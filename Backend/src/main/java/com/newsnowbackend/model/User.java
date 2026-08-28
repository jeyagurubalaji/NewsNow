package com.newsnowbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password; // null for OAuth-only accounts, BCrypt hash otherwise

    private String fullName;

    private String provider; // "LOCAL" or "GOOGLE"

    private String providerId; // Google sub id, if applicable

    private String profileImageUrl;

    @Builder.Default
    private Set<String> roles = new HashSet<>(Set.of("ROLE_USER"));

    // Personalization preferences
    private String preferredCountry; // ISO code, e.g. "in"
    private String preferredLanguage; // ISO 639-1, e.g. "en"

    @Builder.Default
    private List<String> favoriteCategories = List.of();

    @Builder.Default
    private boolean notificationsEnabled = true;

    /** Firebase Cloud Messaging device token, registered by the mobile/web client. Null until set. */
    private String fcmToken;

    @Builder.Default
    private boolean darkMode = false;

    private boolean enabled;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
