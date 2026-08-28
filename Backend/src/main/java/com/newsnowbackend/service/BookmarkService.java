package com.newsnowbackend.service;

import com.newsnowbackend.dto.response.ArticleResponse;
import com.newsnowbackend.exception.ApiException;
import com.newsnowbackend.model.Article;
import com.newsnowbackend.model.Bookmark;
import com.newsnowbackend.repository.ArticleRepository;
import com.newsnowbackend.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;

    public void addBookmark(String userId, String articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw ApiException.notFound("Article not found: " + articleId);
        }
        if (bookmarkRepository.existsByUserIdAndArticleId(userId, articleId)) {
            return; // idempotent
        }
        Bookmark bookmark = Bookmark.builder()
                .userId(userId)
                .articleId(articleId)
                .build();
        bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(String userId, String articleId) {
        bookmarkRepository.deleteByUserIdAndArticleId(userId, articleId);
    }

    public Page<ArticleResponse> getUserBookmarks(String userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "savedAt"));
        Page<Bookmark> bookmarks = bookmarkRepository.findByUserIdOrderBySavedAtDesc(userId, pageable);

        List<String> articleIds = bookmarks.getContent().stream().map(Bookmark::getArticleId).toList();
        List<Article> articles = articleRepository.findAllById(articleIds);

        return bookmarks.map(b -> articles.stream()
                .filter(a -> a.getId().equals(b.getArticleId()))
                .findFirst()
                .map(a -> ArticleResponse.from(a, true))
                .orElse(null));
    }

    public long countBookmarks(String userId) {
        return bookmarkRepository.countByUserId(userId);
    }
}
