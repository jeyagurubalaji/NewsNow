package com.newsnowbackend.service;

import com.newsnowbackend.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

/**
 * Calls an optional external AI microservice (Python/FastAPI, or any LLM API) to generate
 * a 3-4 sentence summary for an article. If AI_SUMMARY_ENABLED=false or the service is
 * unreachable, this degrades gracefully: the article is simply stored without aiSummary,
 * and the frontend falls back to showing the raw description.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiSummaryService {

    private final WebClient webClient;
    private final ArticleRepository articleRepository;

    @Value("${ai.summary.enabled:false}")
    private boolean enabled;

    @Value("${ai.summary.base-url:http://localhost:8000}")
    private String aiServiceBaseUrl;

    @Async
    public void generateSummaryAsync(String articleId, String title, String description) {
        if (!enabled) {
            return;
        }
        if (title == null && description == null) {
            return;
        }

        webClient.post()
                .uri(aiServiceBaseUrl + "/summarize")
                .bodyValue(Map.of(
                        "title", title == null ? "" : title,
                        "description", description == null ? "" : description
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(10))
                .subscribe(
                        result -> {
                            Object summary = result.get("summary");
                            if (summary != null) {
                                articleRepository.findById(articleId).ifPresent(article -> {
                                    article.setAiSummary(summary.toString());
                                    articleRepository.save(article);
                                });
                            }
                        },
                        error -> log.debug("AI summary unavailable for article {}: {}", articleId, error.getMessage())
                );
    }
}
