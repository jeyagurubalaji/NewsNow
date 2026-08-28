package com.newsnowbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Registers or rotates the device's FCM token, sent by the client after
 * Firebase Messaging hands it a token (on first launch, and whenever it refreshes).
 * Kept separate from UpdatePreferencesRequest since tokens rotate independently
 * of user-driven preference changes.
 */
@Data
public class RegisterFcmTokenRequest {

    @NotBlank
    private String fcmToken;
}
