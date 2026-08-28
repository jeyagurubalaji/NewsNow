package com.newsnowbackend.repository;

import com.newsnowbackend.model.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

    Page<Bookmark> findByUserIdOrderBySavedAtDesc(String userId, Pageable pageable);

    Optional<Bookmark> findByUserIdAndArticleId(String userId, String articleId);

    boolean existsByUserIdAndArticleId(String userId, String articleId);

    void deleteByUserIdAndArticleId(String userId, String articleId);

    long countByUserId(String userId);
}
