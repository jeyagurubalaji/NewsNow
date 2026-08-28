package com.newsnowbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Initializes the Firebase Admin SDK for sending breaking-news push notifications,
 * if a service account key is configured. If FCM_ENABLED=false or the key file is
 * missing/unreadable, this logs a warning and leaves FirebaseApp uninitialized —
 * PushNotificationService checks FirebaseApp.getApps() before every send, so the
 * rest of the app (ingestion, auth, everything else) works normally either way.
 */
@Slf4j
@Component
public class FirebaseConfig {

    @Value("${fcm.enabled:false}")
    private boolean enabled;

    @Value("${fcm.service-account-path:}")
    private String serviceAccountPath;

    @PostConstruct
    public void init() {
        if (!enabled) {
            log.info("FCM push notifications disabled (fcm.enabled=false) - breaking news will be stored but not pushed");
            return;
        }
        if (serviceAccountPath == null || serviceAccountPath.isBlank()) {
            log.warn("FCM_ENABLED is true but FCM_SERVICE_ACCOUNT_PATH is not set - push notifications will be skipped");
            return;
        }

        try (FileInputStream serviceAccount = new FileInputStream(serviceAccountPath)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase Admin SDK initialized for push notifications");
            }
        } catch (IOException e) {
            log.error("Could not initialize Firebase Admin SDK from {}: {} - push notifications will be skipped",
                    serviceAccountPath, e.getMessage());
        }
    }
}
