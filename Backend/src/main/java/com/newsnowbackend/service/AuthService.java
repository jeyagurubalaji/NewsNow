package com.newsnowbackend.service;

import com.newsnowbackend.dto.request.GoogleAuthRequest;
import com.newsnowbackend.dto.request.LoginRequest;
import com.newsnowbackend.dto.request.RegisterRequest;
import com.newsnowbackend.dto.response.AuthResponse;
import com.newsnowbackend.dto.response.UserResponse;
import com.newsnowbackend.exception.ApiException;
import com.newsnowbackend.model.User;
import com.newsnowbackend.repository.UserRepository;
import com.newsnowbackend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;

    private static final String GOOGLE_TOKENINFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw ApiException.conflict("An account with this email already exists");
        }

        User user = User.builder()
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .provider("LOCAL")
                .roles(new HashSet<>(Set.of("ROLE_USER")))
                .preferredCountry(request.getPreferredCountry() != null ? request.getPreferredCountry() : "us")
                .preferredLanguage(request.getPreferredLanguage() != null ? request.getPreferredLanguage() : "en")
                .favoriteCategories(java.util.List.of())
                .notificationsEnabled(true)
                .darkMode(false)
                .enabled(true)
                .build();

        User saved = userRepository.save(user);
        return buildAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> ApiException.unauthorized("Invalid email or password"));

        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw ApiException.unauthorized("Invalid email or password");
        }

        return buildAuthResponse(user);
    }

    @SuppressWarnings("unchecked")
    public AuthResponse googleLogin(GoogleAuthRequest request) {
        Map<String, Object> tokenInfo;
        try {
            tokenInfo = webClient.get()
                    .uri(GOOGLE_TOKENINFO_URL + request.getIdToken())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            throw new ApiException("Invalid Google ID token", HttpStatus.UNAUTHORIZED);
        }

        if (tokenInfo == null || tokenInfo.get("email") == null) {
            throw new ApiException("Invalid Google ID token", HttpStatus.UNAUTHORIZED);
        }

        String email = ((String) tokenInfo.get("email")).toLowerCase();
        String googleSub = (String) tokenInfo.get("sub");
        String name = (String) tokenInfo.getOrDefault("name", email);
        String picture = (String) tokenInfo.get("picture");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .fullName(name)
                    .provider("GOOGLE")
                    .providerId(googleSub)
                    .profileImageUrl(picture)
                    .roles(new HashSet<>(Set.of("ROLE_USER")))
                    .preferredCountry("us")
                    .preferredLanguage("en")
                    .favoriteCategories(java.util.List.of())
                    .notificationsEnabled(true)
                    .darkMode(false)
                    .enabled(true)
                    .build();
            return userRepository.save(newUser);
        });

        return buildAuthResponse(user);
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken) || !"refresh".equals(jwtUtil.extractTokenType(refreshToken))) {
            throw ApiException.unauthorized("Invalid or expired refresh token");
        }
        String userId = jwtUtil.extractUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.unauthorized("User no longer exists"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(UserResponse.from(user))
                .build();
    }
}
