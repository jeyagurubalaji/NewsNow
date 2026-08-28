package com.newsnowbackend.constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Subset of ISO 639-1 language codes supported by newsdata.io for the `language` filter.
 * newsdata.io supports ~100 languages; the most commonly requested ones are listed here.
 * Extend freely - the value is only used for display + passthrough to the provider.
 */
public final class LanguageConstants {

    private LanguageConstants() {
    }

    public static final Map<String, String> LANGUAGES = new LinkedHashMap<>();
    public static final String DEFAULT_LANGUAGE = "en";

    static {
        LANGUAGES.put("en", "English");
        LANGUAGES.put("hi", "Hindi");
        LANGUAGES.put("es", "Spanish");
        LANGUAGES.put("fr", "French");
        LANGUAGES.put("de", "German");
        LANGUAGES.put("zh", "Chinese");
        LANGUAGES.put("ar", "Arabic");
        LANGUAGES.put("pt", "Portuguese");
        LANGUAGES.put("ru", "Russian");
        LANGUAGES.put("ja", "Japanese");
        LANGUAGES.put("ko", "Korean");
        LANGUAGES.put("it", "Italian");
        LANGUAGES.put("nl", "Dutch");
        LANGUAGES.put("tr", "Turkish");
        LANGUAGES.put("vi", "Vietnamese");
        LANGUAGES.put("th", "Thai");
        LANGUAGES.put("id", "Indonesian");
        LANGUAGES.put("ta", "Tamil");
        LANGUAGES.put("te", "Telugu");
        LANGUAGES.put("bn", "Bengali");
        LANGUAGES.put("ur", "Urdu");
        LANGUAGES.put("mr", "Marathi");
        LANGUAGES.put("pl", "Polish");
        LANGUAGES.put("uk", "Ukrainian");
        LANGUAGES.put("sw", "Swahili");
        LANGUAGES.put("fa", "Persian");
        LANGUAGES.put("he", "Hebrew");
        LANGUAGES.put("el", "Greek");
        LANGUAGES.put("sv", "Swedish");
        LANGUAGES.put("ro", "Romanian");
        // ...extend up to ~100 as needed; newsdata.io accepts the full ISO 639-1 set.
    }
}
