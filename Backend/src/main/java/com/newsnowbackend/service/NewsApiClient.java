package com.newsnowbackend.service;

import com.newsnowbackend.dto.external.NewsDataApiResponse;
import com.newsnowbackend.model.NewsCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.net.URI;

/**
 * Thin client around newsdata.io's REST API.
 * Docs: https://newsdata.io/documentation
 *
 * Free tier notes: rate-limited requests/day, results paginated 10 at a time via `page`/`nextPage`
 * cursor tokens. This client fetches a single page per call; the scheduler controls pacing
 * across the 195 supported countries to stay within quota.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NewsApiClient {

    private final WebClient webClient;

    @Value("${newsapi.base-url}")
    private String baseUrl;

    @Value("${newsapi.api-key}")
    private String apiKey;

    /**
     * Fetch latest headlines for a given country/category/language combination.
     *
     * @param countryCode ISO 3166-1 alpha-2 code, e.g. "in"
     * @param category    NewsCategory (mapped to provider's category string); null/"top" = general
     * @param language    ISO 639-1 code, e.g. "en"; may be null for provider default
     */
    public Mono<NewsDataApiResponse> fetchHeadlines(String countryCode, NewsCategory category, String language) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("NEWSDATA_API_KEY is not configured - skipping live fetch for country={}", countryCode);
            return Mono.empty();
        }

        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, countryCode, category, language, null))
                .retrieve()
                .bodyToMono(NewsDataApiResponse.class)
                .timeout(Duration.ofSeconds(20))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
                        .filter(this::isRetryable))
                .doOnError(e -> log.error("newsdata.io fetch failed for country={} category={}: {}",
                        countryCode, category, e.getMessage()));
    }

    public Mono<NewsDataApiResponse> searchNews(String keyword, String countryCode, String language, String nextPage) {
        if (apiKey == null || apiKey.isBlank()) {
            return Mono.empty();
        }

        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/news")
                            .queryParam("apikey", apiKey)
                            .queryParam("q", keyword);
                    if (countryCode != null) builder.queryParam("country", countryCode);
                    if (language != null) builder.queryParam("language", language);
                    if (nextPage != null) builder.queryParam("page", nextPage);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(NewsDataApiResponse.class)
                .timeout(Duration.ofSeconds(20));
    }

    private URI buildUri(UriBuilder uriBuilder, String countryCode, NewsCategory category, String language, String page) {
        var builder = uriBuilder.path("/latest")
                .queryParam("apikey", apiKey)
                .queryParam("country", countryCode);

        if (category != null && category != NewsCategory.TOP) {
            builder.queryParam("category", category.getProviderValue());
        }
        if (language != null && !language.isBlank()) {
            builder.queryParam("language", language);
        }
        if (page != null) {
            builder.queryParam("page", page);
        }
        return builder.build();
    }

    private boolean isRetryable(Throwable throwable) {
        String msg = throwable.getMessage();
        // Retry on transient network/5xx errors, not on 401/422 (bad key / bad params)
        return msg == null || (!msg.contains("401") && !msg.contains("422"));
    }
}
