package com.newsnowbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * newsdata.io (and most news APIs) don't flag articles as "breaking" themselves —
 * that's an editorial judgment, not metadata. This heuristic approximates it so the
 * breaking-news ticker in both frontends has something real to show:
 *
 *   an article counts as breaking if it was published within the last N minutes
 *   (default 45) AND its title or description contains an urgent-news signal word.
 *
 * This is intentionally conservative (recency + keyword, not just keyword alone) to
 * avoid flagging old syndicated wire content that happens to mention "crisis" in
 * a headline written last week. It will have false negatives/positives like any
 * heuristic — replacing it with a real editorial/ML signal is a reasonable next step.
 */
@Component
public class BreakingNewsDetector {

    @Value("${breaking.detection.window-minutes:45}")
    private int windowMinutes;

    private static final List<String> SIGNAL_WORDS = List.of(
            "breaking", "just in", "live updates", "developing", "urgent",
            "state of emergency", "evacuat", "explosion", "earthquake", "wildfire",
            "gunman", "shooting", "attack", "killed", "dead", "casualties",
            "crash", "collapse", "flooding", "tsunami", "hurricane", "cyclone",
            "coup", "assassinat", "resign", "declares war", "ceasefire"
    );

    public boolean isBreaking(String title, String description, Instant publishedAt) {
        if (publishedAt == null) {
            return false;
        }
        long minutesAgo = ChronoUnit.MINUTES.between(publishedAt, Instant.now());
        if (minutesAgo < 0 || minutesAgo > windowMinutes) {
            return false;
        }

        String haystack = ((title == null ? "" : title) + " " + (description == null ? "" : description))
                .toLowerCase(Locale.ROOT);

        return SIGNAL_WORDS.stream().anyMatch(haystack::contains);
    }
}
