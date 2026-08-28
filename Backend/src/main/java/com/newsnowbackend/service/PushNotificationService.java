package com.newsnowbackend.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import com.newsnowbackend.model.Article;
import com.newsnowbackend.model.User;
import com.newsnowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Sends a push notification via Firebase Cloud Messaging to every user who has
 * notificationsEnabled=true and a registered device token, whenever the ingestion
 * pipeline flags a newly-fetched article as breaking (see BreakingNewsDetector).
 *
 * No-ops entirely if Firebase wasn't initialized (see FirebaseConfig) - ingestion
 * never fails or blocks because of a missing/misconfigured push setup.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final UserRepository userRepository;

    private static final int FCM_BATCH_SIZE = 500; // FCM's multicast limit per request

    @Async
    public void notifyBreakingArticle(Article article) {
        if (FirebaseApp.getApps().isEmpty()) {
            return; // Firebase not configured - see FirebaseConfig
        }

        List<User> recipients = userRepository.findByNotificationsEnabledTrueAndFcmTokenIsNotNull();
        if (recipients.isEmpty()) {
            return;
        }

        List<String> tokens = recipients.stream().map(User::getFcmToken).toList();

        for (List<String> batch : partition(tokens, FCM_BATCH_SIZE)) {
            sendBatch(batch, article);
        }
    }

    private void sendBatch(List<String> tokens, Article article) {
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle("Breaking · " + article.getCountry().toUpperCase())
                        .setBody(article.getTitle())
                        .build())
                .putData("articleId", article.getId())
                .putData("type", "breaking_news")
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            if (response.getFailureCount() > 0) {
                logFailures(tokens, response);
            }
            log.info("Sent breaking-news push for article {} to {} devices ({} failed)",
                    article.getId(), tokens.size(), response.getFailureCount());
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send breaking-news push for article {}: {}", article.getId(), e.getMessage());
        }
    }

    private void logFailures(List<String> tokens, BatchResponse response) {
        List<SendResponse> responses = response.getResponses();
        for (int i = 0; i < responses.size(); i++) {
            if (!responses.get(i).isSuccessful()) {
                // Token likely uninstalled/expired; a production system would prune it here
                // via userRepository, keyed off the token at this index.
                log.debug("Push failed for token ending in ...{}: {}",
                        tokens.get(i).length() > 6 ? tokens.get(i).substring(tokens.get(i).length() - 6) : tokens.get(i),
                        responses.get(i).getException() != null ? responses.get(i).getException().getMessage() : "unknown");
            }
        }
    }

    private static List<List<String>> partition(List<String> list, int size) {
        List<List<String>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}
