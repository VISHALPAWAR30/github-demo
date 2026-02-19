package com.example.user_profile.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String getCurrentUserId(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated request");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getSubject();
    }
}
