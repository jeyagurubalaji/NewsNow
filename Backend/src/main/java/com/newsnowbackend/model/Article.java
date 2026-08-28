package com.newsnowbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "articles")
@CompoundIndexes({
        @CompoundIndex(name = "country_category_idx", def = "{'country': 1, 'category': 1, 'publishedAt': -1}"),
        @CompoundIndex(name = "country_lang_idx", def = "{'country': 1, 'language': 1}")
})
public class Article {

    @Id
    private String id;

    /**
     * Provider's own article id/hash - used to de-duplicate on upsert.
     */
    @Indexed(unique = true)
    private String externalId;

    private String title;

    private String description;

    private String content;

    /** 3-4 sentence AI generated summary, populated asynchronously */
    private String aiSummary;

    private String url;

    private String imageUrl;

    private String sourceId;

    private String sourceName;

    @Indexed
    private String country; // ISO code this article was fetched for

    @Indexed
    private String language; // ISO 639-1

    @Indexed
    private String category; // NewsCategory providerValue

    private List<String> keywords;

    private Instant publishedAt;

    @CreatedDate
    private Instant fetchedAt;

    private boolean breaking;
}
