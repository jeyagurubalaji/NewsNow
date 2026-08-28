package com.newsnowbackend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePreferencesRequest {
    private String preferredCountry;
    private String preferredLanguage;
    private List<String> favoriteCategories;
    private Boolean notificationsEnabled;
    private Boolean darkMode;
}
