package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class JWTService {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String userId, String email, List<String> roles) {

        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(
                        Keys.hmacShaKeyFor(secret.getBytes()),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }
}
