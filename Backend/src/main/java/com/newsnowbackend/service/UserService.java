package com.newsnowbackend.service;

import com.newsnowbackend.constants.CountryConstants;
import com.newsnowbackend.dto.request.RegisterFcmTokenRequest;
import com.newsnowbackend.dto.request.UpdatePreferencesRequest;
import com.newsnowbackend.dto.response.UserResponse;
import com.newsnowbackend.exception.ApiException;
import com.newsnowbackend.model.User;
import com.newsnowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getProfile(String userId) {
        return UserResponse.from(findUser(userId));
    }

    public UserResponse updatePreferences(String userId, UpdatePreferencesRequest request) {
        User user = findUser(userId);

        if (request.getPreferredCountry() != null) {
            if (!CountryConstants.isSupported(request.getPreferredCountry())) {
                throw ApiException.badRequest("Unsupported country code: " + request.getPreferredCountry());
            }
            user.setPreferredCountry(request.getPreferredCountry().toLowerCase());
        }
        if (request.getPreferredLanguage() != null) {
            user.setPreferredLanguage(request.getPreferredLanguage().toLowerCase());
        }
        if (request.getFavoriteCategories() != null) {
            user.setFavoriteCategories(request.getFavoriteCategories());
        }
        if (request.getNotificationsEnabled() != null) {
            user.setNotificationsEnabled(request.getNotificationsEnabled());
        }
        if (request.getDarkMode() != null) {
            user.setDarkMode(request.getDarkMode());
        }

        return UserResponse.from(userRepository.save(user));
    }

    /** Registers/rotates the device's FCM token so breaking-news pushes can reach it. */
    public void registerFcmToken(String userId, RegisterFcmTokenRequest request) {
        User user = findUser(userId);
        user.setFcmToken(request.getFcmToken());
        userRepository.save(user);
    }

    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));
    }
}
