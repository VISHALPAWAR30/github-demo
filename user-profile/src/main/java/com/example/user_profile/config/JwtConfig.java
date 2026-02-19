package com.example.user_profile.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class JwtConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secret;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key =
                new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}
