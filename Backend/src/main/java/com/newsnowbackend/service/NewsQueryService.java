package com.newsnowbackend.service;

import com.newsnowbackend.constants.CountryConstants;
import com.newsnowbackend.dto.response.ArticleResponse;
import com.newsnowbackend.exception.ApiException;
import com.newsnowbackend.model.Article;
import com.newsnowbackend.model.NewsCategory;
import com.newsnowbackend.repository.ArticleRepository;
import com.newsnowbackend.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsQueryService {

    private final ArticleRepository articleRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * Core headline fetch: country is required (this is a country-first product);
     * category and language are optional filters.
     */
    @Cacheable(cacheNames = "headlines", key = "#country + ':' + #category + ':' + #language + ':' + #page + ':' + #size")
    public Page<Article> getHeadlines(String country, String category, String language, int page, int size) {
        String normalizedCountry = validateCountry(country);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));

        boolean hasCategory = category != null && !category.isBlank() && !"top".equalsIgnoreCase(category);
        boolean hasLanguage = language != null && !language.isBlank();

        if (hasCategory && hasLanguage) {
            NewsCategory cat = NewsCategory.fromString(category);
            return articleRepository.findByCountryAndCategoryAndLanguageOrderByPublishedAtDesc(
                    normalizedCountry, cat.getProviderValue(), language, pageable);
        } else if (hasCategory) {
            NewsCategory cat = NewsCategory.fromString(category);
            return articleRepository.findByCountryAndCategoryOrderByPublishedAtDesc(
                    normalizedCountry, cat.getProviderValue(), pageable);
        } else if (hasLanguage) {
            return articleRepository.findByCountryAndLanguageOrderByPublishedAtDesc(
                    normalizedCountry, language, pageable);
        } else {
            return articleRepository.findByCountryOrderByPublishedAtDesc(normalizedCountry, pageable);
        }
    }

    public Page<Article> searchNews(String keyword, String country, int page, int size) {
        if (keyword == null || keyword.isBlank()) {
            throw ApiException.badRequest("Search keyword must not be empty");
        }
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));

        if (country != null && !country.isBlank()) {
            return articleRepository.searchByCountryAndKeyword(validateCountry(country), keyword, pageable);
        }
        return articleRepository.searchByKeyword(keyword, pageable);
    }

    public List<Article> getBreakingNews() {
        Instant since = Instant.now().minus(6, ChronoUnit.HOURS);
        return articleRepository.findByBreakingTrueAndPublishedAtAfterOrderByPublishedAtDesc(since);
    }

    public Article getArticleById(String id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Article not found: " + id));
    }

    /** Maps an Article to its API response, tagging whether the given user has bookmarked it. */
    public ArticleResponse toResponse(Article article, String userId) {
        boolean bookmarked = userId != null &&
                bookmarkRepository.existsByUserIdAndArticleId(userId, article.getId());
        return ArticleResponse.from(article, bookmarked);
    }

    private String validateCountry(String country) {
        if (country == null || !CountryConstants.isSupported(country)) {
            throw ApiException.badRequest("Unsupported or missing country code: " + country
                    + ". Must be one of the 195 supported ISO 3166-1 alpha-2 codes.");
        }
        return country.toLowerCase();
    }
}
