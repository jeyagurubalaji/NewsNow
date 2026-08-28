package com.newsnowbackend.dto.response;

import com.newsnowbackend.model.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private String id;
    private String title;
    private String description;
    private String aiSummary;
    private String url;
    private String imageUrl;
    private String sourceName;
    private String country;
    private String language;
    private String category;
    private Instant publishedAt;
    private boolean breaking;
    private boolean bookmarked;

    public static ArticleResponse from(Article a, boolean bookmarked) {
        return ArticleResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .description(a.getDescription())
                .aiSummary(a.getAiSummary())
                .url(a.getUrl())
                .imageUrl(a.getImageUrl())
                .sourceName(a.getSourceName())
                .country(a.getCountry())
                .language(a.getLanguage())
                .category(a.getCategory())
                .publishedAt(a.getPublishedAt())
                .breaking(a.isBreaking())
                .bookmarked(bookmarked)
                .build();
    }
}
