package com.newsnowbackend.controller;

import com.newsnowbackend.constants.CountryConstants;
import com.newsnowbackend.constants.LanguageConstants;
import com.newsnowbackend.dto.response.ArticleResponse;
import com.newsnowbackend.model.Article;
import com.newsnowbackend.model.NewsCategory;
import com.newsnowbackend.security.CustomUserDetails;
import com.newsnowbackend.service.NewsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsQueryService newsQueryService;

    /** GET /api/news/headlines/{country}?category=&language=&page=&size= */
    @GetMapping("/headlines/{country}")
    public ResponseEntity<Page<ArticleResponse>> getHeadlines(
            @PathVariable String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Page<Article> articles = newsQueryService.getHeadlines(country, category, language, page, size);
        String userId = principal != null ? principal.getId() : null;
        Page<ArticleResponse> response = articles.map(a -> newsQueryService.toResponse(a, userId));
        return ResponseEntity.ok(response);
    }

    /** GET /api/news/search?q=&country=&page=&size= */
    @GetMapping("/search")
    public ResponseEntity<Page<ArticleResponse>> search(
            @RequestParam("q") String keyword,
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Page<Article> results = newsQueryService.searchNews(keyword, country, page, size);
        String userId = principal != null ? principal.getId() : null;
        return ResponseEntity.ok(results.map(a -> newsQueryService.toResponse(a, userId)));
    }

    /** GET /api/news/breaking - articles flagged breaking in the last 6 hours */
    @GetMapping("/breaking")
    public ResponseEntity<List<ArticleResponse>> breakingNews(@AuthenticationPrincipal CustomUserDetails principal) {
        String userId = principal != null ? principal.getId() : null;
        List<ArticleResponse> response = newsQueryService.getBreakingNews().stream()
                .map(a -> newsQueryService.toResponse(a, userId))
                .toList();
        return ResponseEntity.ok(response);
    }

    /** GET /api/news/article/{id} */
    @GetMapping("/article/{id}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable String id,
                                                        @AuthenticationPrincipal CustomUserDetails principal) {
        Article article = newsQueryService.getArticleById(id);
        String userId = principal != null ? principal.getId() : null;
        return ResponseEntity.ok(newsQueryService.toResponse(article, userId));
    }

    /** GET /api/news/countries - all 195 supported countries for the picker UI */
    @GetMapping("/countries")
    @Cacheable(cacheNames = "staticLists", key = "'countries'")
    public ResponseEntity<Map<String, String>> getCountries() {
        return ResponseEntity.ok(CountryConstants.COUNTRIES);
    }

    /** GET /api/news/categories */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(Arrays.stream(NewsCategory.values()).map(NewsCategory::getProviderValue).toList());
    }

    /** GET /api/news/languages */
    @GetMapping("/languages")
    @Cacheable(cacheNames = "staticLists", key = "'languages'")
    public ResponseEntity<Map<String, String>> getLanguages() {
        return ResponseEntity.ok(LanguageConstants.LANGUAGES);
    }
}
