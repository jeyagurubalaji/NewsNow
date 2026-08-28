package com.newsnowbackend.service;

import com.newsnowbackend.constants.CountryConstants;
import com.newsnowbackend.model.NewsCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Drives automatic, rotating news refresh across all 195 supported countries.
 *
 * Rationale: free/starter tiers of newsdata.io enforce a daily request quota, so fetching
 * "top" headlines for all 195 countries on every tick would burn through quota fast.
 * Instead, this scheduler rotates through countries in fixed-size batches on each tick
 * (default: every 30 minutes, 15 countries/tick -> full 195-country sweep every ~6.5 hours).
 *
 * Breaking-news-sensitive large countries can be prioritized by listing them first in
 * PRIORITY_COUNTRIES; they get refreshed on every tick in addition to the rotating batch.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsSchedulerService {

    private final NewsIngestionService newsIngestionService;

    @Value("${scheduler.enabled:true}")
    private boolean schedulerEnabled;

    @Value("${scheduler.batch-size:15}")
    private int batchSize;

    // High-traffic countries refreshed on every tick regardless of rotation position
    private static final List<String> PRIORITY_COUNTRIES = List.of(
            "us", "in", "gb", "au", "ca", "de", "fr", "jp", "cn", "br"
    );

    private final List<String> allCountryCodes = new ArrayList<>(CountryConstants.COUNTRIES.keySet());
    private final AtomicInteger rotationCursor = new AtomicInteger(0);

    /**
     * Main automatic refresh job. Cron pattern is externalized in application.yml
     * (default: every 30 minutes). Fetches "top" headlines per country; category-specific
     * refresh is handled by refreshCategoriesForPriorityCountries().
     */
    @Scheduled(cron = "${scheduler.refresh-cron:0 */30 * * * *}")
    public void scheduledRefresh() {
        if (!schedulerEnabled) {
            log.debug("Scheduler disabled via configuration - skipping tick");
            return;
        }

        log.info("Starting scheduled news refresh tick (rotationCursor={})", rotationCursor.get());

        // 1. Always refresh priority countries' top headlines
        for (String country : PRIORITY_COUNTRIES) {
            safeIngest(country, NewsCategory.TOP, null);
        }

        // 2. Refresh the next rotating batch out of all 195 countries
        List<String> batch = nextRotationBatch();
        for (String country : batch) {
            if (!PRIORITY_COUNTRIES.contains(country)) {
                safeIngest(country, NewsCategory.TOP, null);
            }
        }

        log.info("Completed scheduled refresh tick: {} priority + {} rotating countries",
                PRIORITY_COUNTRIES.size(), batch.size());
    }

    /**
     * Secondary job: refreshes category-specific headlines (politics, business, tech, etc.)
     * for priority countries only, on a slower cadence, to keep category filters fresh
     * without multiplying API calls across all 195 countries x 8 categories.
     */
    @Scheduled(cron = "${scheduler.category-refresh-cron:0 0 */2 * * *}") // every 2 hours
    public void refreshCategoriesForPriorityCountries() {
        if (!schedulerEnabled) {
            return;
        }
        log.info("Starting category refresh for priority countries");
        for (String country : PRIORITY_COUNTRIES) {
            for (NewsCategory category : NewsCategory.values()) {
                if (category == NewsCategory.TOP) continue;
                safeIngest(country, category, null);
            }
        }
    }

    /** Nightly cleanup of stale articles beyond 7 days to keep the collection lean. */
    @Scheduled(cron = "${scheduler.purge-cron:0 0 3 * * *}") // 3 AM daily
    public void purgeStaleArticles() {
        newsIngestionService.purgeOlderThan(7);
    }

    /** Manually trigger a refresh for a single country - used by the on-demand admin endpoint. */
    public int refreshCountryNow(String countryCode, NewsCategory category, String language) {
        return newsIngestionService.ingestForCountry(countryCode, category, language);
    }

    private List<String> nextRotationBatch() {
        int total = allCountryCodes.size();
        int start = rotationCursor.getAndUpdate(cursor -> (cursor + batchSize) % total);
        List<String> batch = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            batch.add(allCountryCodes.get((start + i) % total));
        }
        return batch;
    }

    private void safeIngest(String country, NewsCategory category, String language) {
        try {
            newsIngestionService.ingestForCountry(country, category, language);
        } catch (Exception e) {
            log.error("Failed to ingest news for country={} category={}: {}", country, category, e.getMessage());
        }
    }
}
