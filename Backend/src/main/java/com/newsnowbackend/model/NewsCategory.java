package com.newsnowbackend.model;

public enum NewsCategory {
    POLITICS("politics"),
    BUSINESS("business"),
    TECHNOLOGY("technology"),
    SPORTS("sports"),
    ENTERTAINMENT("entertainment"),
    SCIENCE("science"),
    HEALTH("health"),
    WORLD("world"),
    TOP("top"); // newsdata.io default/general top-headlines category

    private final String providerValue;

    NewsCategory(String providerValue) {
        this.providerValue = providerValue;
    }

    public String getProviderValue() {
        return providerValue;
    }

    public static NewsCategory fromString(String value) {
        if (value == null) return TOP;
        for (NewsCategory c : values()) {
            if (c.providerValue.equalsIgnoreCase(value) || c.name().equalsIgnoreCase(value)) {
                return c;
            }
        }
        return TOP;
    }
}
