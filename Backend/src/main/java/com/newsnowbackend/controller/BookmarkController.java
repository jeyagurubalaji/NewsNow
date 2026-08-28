package com.newsnowbackend.controller;

import com.newsnowbackend.dto.response.ArticleResponse;
import com.newsnowbackend.security.CustomUserDetails;
import com.newsnowbackend.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{articleId}")
    public ResponseEntity<Void> addBookmark(@PathVariable String articleId,
                                             @AuthenticationPrincipal CustomUserDetails principal) {
        bookmarkService.addBookmark(principal.getId(), articleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable String articleId,
                                                @AuthenticationPrincipal CustomUserDetails principal) {
        bookmarkService.removeBookmark(principal.getId(), articleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> getBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(principal.getId(), page, size));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countBookmarks(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(Map.of("count", bookmarkService.countBookmarks(principal.getId())));
    }
}
