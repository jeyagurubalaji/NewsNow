package com.newsnowbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookmarks")
@CompoundIndexes({
        @CompoundIndex(name = "user_article_unique", def = "{'userId': 1, 'articleId': 1}", unique = true)
})
public class Bookmark {

    @Id
    private String id;

    private String userId;

    private String articleId;

    @CreatedDate
    private Instant savedAt;
}
