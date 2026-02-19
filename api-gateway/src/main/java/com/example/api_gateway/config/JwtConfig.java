package com.example.api_gateway.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class JwtConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secret;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        SecretKey key = new SecretKeySpec(
                secret.getBytes(),
                "HmacSHA256"
        );
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
