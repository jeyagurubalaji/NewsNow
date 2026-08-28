package com.newsnow.dto.response;

import com.newsnow.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private String preferredCountry;
    private String preferredLanguage;
    private List<String> favoriteCategories;
    private boolean notificationsEnabled;
    private boolean darkMode;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profileImageUrl(user.getProfileImageUrl())
                .preferredCountry(user.getPreferredCountry())
                .preferredLanguage(user.getPreferredLanguage())
                .favoriteCategories(user.getFavoriteCategories())
                .notificationsEnabled(user.isNotificationsEnabled())
                .darkMode(user.isDarkMode())
                .build();
    }
}
