package com.example.document_wallet.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static String getCurrentUserId(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated request");
        }

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new RuntimeException("Invalid authentication type");
        }

        return jwtAuth.getToken().getSubject();
    }
}
