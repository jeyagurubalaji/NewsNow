package com.newsnowbackend.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Maps the JSON response returned by https://newsdata.io/api/1/news (and /latest).
 * Only the fields NewsNow actually uses are mapped; unknown fields are ignored.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsDataApiResponse {

    private String status;

    @JsonProperty("totalResults")
    private long totalResults;

    private List<NewsDataArticle> results;

    @JsonProperty("nextPage")
    private String nextPage;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsDataArticle {

        @JsonProperty("article_id")
        private String articleId;

        private String title;

        private String link;

        private String description;

        private String content;

        @JsonProperty("pubDate")
        private String pubDate;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("source_id")
        private String sourceId;

        @JsonProperty("source_name")
        private String sourceName;

        private List<String> country;

        private String language;

        private List<String> category;

        private List<String> keywords;
    }
}
