package com.newsnowbackend.repository;

import com.newsnowbackend.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String> {

    Optional<Article> findByExternalId(String externalId);

    boolean existsByExternalId(String externalId);

    Page<Article> findByCountryOrderByPublishedAtDesc(String country, Pageable pageable);

    Page<Article> findByCountryAndCategoryOrderByPublishedAtDesc(String country, String category, Pageable pageable);

    Page<Article> findByCountryAndLanguageOrderByPublishedAtDesc(String country, String language, Pageable pageable);

    Page<Article> findByCountryAndCategoryAndLanguageOrderByPublishedAtDesc(
            String country, String category, String language, Pageable pageable);

    @Query("{ 'country': ?0, '$or': [ { 'title': { $regex: ?1, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ] }")
    Page<Article> searchByCountryAndKeyword(String country, String keyword, Pageable pageable);

    @Query("{ '$or': [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }")
    Page<Article> searchByKeyword(String keyword, Pageable pageable);

    List<Article> findByBreakingTrueAndPublishedAtAfterOrderByPublishedAtDesc(Instant since);

    Page<Article> findByCategoryOrderByPublishedAtDesc(String category, Pageable pageable);

    void deleteByPublishedAtBefore(Instant cutoff);
}
