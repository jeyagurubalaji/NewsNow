package com.newsnowbackend.controller;

import com.newsnowbackend.model.NewsCategory;
import com.newsnowbackend.service.NewsSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Admin-only endpoints, gated by ROLE_ADMIN in SecurityConfig.
 * Useful for on-demand refresh during development/testing instead of waiting for the
 * scheduled job, and for manually refreshing a specific country after launch.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final NewsSchedulerService newsSchedulerService;

    @PostMapping("/refresh/{country}")
    public ResponseEntity<Map<String, Object>> refreshCountry(
            @PathVariable String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String language) {

        NewsCategory cat = category != null ? NewsCategory.fromString(category) : NewsCategory.TOP;
        int count = newsSchedulerService.refreshCountryNow(country.toLowerCase(), cat, language);

        return ResponseEntity.ok(Map.of(
                "country", country,
                "category", cat.getProviderValue(),
                "newArticlesIngested", count
        ));
    }
}
