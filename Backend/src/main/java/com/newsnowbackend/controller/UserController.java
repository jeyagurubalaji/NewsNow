package com.newsnowbackend.controller;

import com.newsnowbackend.dto.request.RegisterFcmTokenRequest;
import com.newsnowbackend.dto.request.UpdatePreferencesRequest;
import com.newsnowbackend.dto.response.UserResponse;
import com.newsnowbackend.security.CustomUserDetails;
import com.newsnowbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(userService.getProfile(principal.getId()));
    }

    @PatchMapping("/me/preferences")
    public ResponseEntity<UserResponse> updatePreferences(
            @RequestBody UpdatePreferencesRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(userService.updatePreferences(principal.getId(), request));
    }

    /** Registers/rotates this device's FCM token so breaking-news pushes can reach it. */
    @PostMapping("/me/fcm-token")
    public ResponseEntity<Void> registerFcmToken(
            @Valid @RequestBody RegisterFcmTokenRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        userService.registerFcmToken(principal.getId(), request);
        return ResponseEntity.ok().build();
    }
}
