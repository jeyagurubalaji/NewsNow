package com.newsnowbackend.service;

import com.newsnowbackend.dto.external.NewsDataApiResponse;
import com.newsnowbackend.model.Article;
import com.newsnowbackend.model.NewsCategory;
import com.newsnowbackend.repository.ArticleRepository;
import com.newsnowbackend.service.NewsApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsIngestionService {

    private final NewsApiClient newsApiClient;
    private final ArticleRepository articleRepository;
    private final AiSummaryService aiSummaryService;
    private final BreakingNewsDetector breakingNewsDetector;
    private final PushNotificationService pushNotificationService;

    private static final DateTimeFormatter PROVIDER_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Fetches the latest headlines for one country/category/language combination
     * and upserts them into MongoDB. Called by the scheduler in rotating batches,
     * and can also be called on-demand (e.g. admin "refresh now" endpoint).
     */
    @CacheEvict(cacheNames = "headlines", allEntries = true)
    public int ingestForCountry(String countryCode, NewsCategory category, String language) {
        NewsDataApiResponse response = newsApiClient
                .fetchHeadlines(countryCode, category, language)
                .block(); // scheduler runs on its own thread; blocking here is acceptable & simple

        if (response == null || response.getResults() == null) {
            return 0;
        }

        int savedCount = 0;
        for (NewsDataApiResponse.NewsDataArticle raw : response.getResults()) {
            try {
                Article article = mapToArticle(raw, countryCode, category, language);
                if (article.getExternalId() == null) {
                    continue;
                }
                // Upsert semantics: skip if we already have this exact article
                if (!articleRepository.existsByExternalId(article.getExternalId())) {
                    Article saved = articleRepository.save(article);
                    savedCount++;
                    aiSummaryService.generateSummaryAsync(saved.getId(), saved.getTitle(), saved.getDescription());
                    if (saved.isBreaking()) {
                        pushNotificationService.notifyBreakingArticle(saved);
                    }
                }
            } catch (Exception e) {
                log.warn("Skipping malformed article from provider: {}", e.getMessage());
            }
        }

        log.info("Ingested {} new articles for country={} category={} language={}",
                savedCount, countryCode, category, language);
        return savedCount;
    }

    private Article mapToArticle(NewsDataApiResponse.NewsDataArticle raw, String countryCode,
                                  NewsCategory category, String language) {
        Instant publishedAt = parseDate(raw.getPubDate());
        boolean breaking = breakingNewsDetector.isBreaking(raw.getTitle(), raw.getDescription(), publishedAt);

        return Article.builder()
                .externalId(raw.getArticleId())
                .title(raw.getTitle())
                .description(raw.getDescription())
                .content(raw.getContent())
                .url(raw.getLink())
                .imageUrl(raw.getImageUrl())
                .sourceId(raw.getSourceId())
                .sourceName(raw.getSourceName())
                .country(countryCode)
                .language(Objects.requireNonNullElse(raw.getLanguage(), language))
                .category(category != null ? category.getProviderValue() : NewsCategory.TOP.getProviderValue())
                .keywords(raw.getKeywords())
                .publishedAt(publishedAt)
                .breaking(breaking)
                .build();
    }

    private Instant parseDate(String pubDate) {
        if (pubDate == null || pubDate.isBlank()) {
            return Instant.now();
        }
        try {
            return java.time.LocalDateTime.parse(pubDate, PROVIDER_DATE_FORMAT).toInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            return Instant.now();
        }
    }

    /** Purge articles older than N days to keep the collection lean. */
    public void purgeOlderThan(int days) {
        Instant cutoff = Instant.now().minusSeconds(days * 24L * 3600);
        articleRepository.deleteByPublishedAtBefore(cutoff);
        log.info("Purged articles older than {} days (cutoff={})", days, cutoff);
    }
}
