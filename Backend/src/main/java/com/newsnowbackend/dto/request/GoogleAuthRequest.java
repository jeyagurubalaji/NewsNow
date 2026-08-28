package com.newsnowbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleAuthRequest {

    /** The Google ID token obtained by the frontend via Google Identity Services. */
    @NotBlank
    private String idToken;
}
