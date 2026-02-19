package com.example.document_wallet.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
@Configuration
public class JwtConfig {

//    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
//    private String secret;

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(
                new SecretKeySpec(
                        "FbBQTrXZOClFM2YlPDywnN+enwj2TnXTaAchCgyjj+o=".getBytes(),
                        "HmacSHA256"
                )
        ).build();
    }
}
