package com.madi.backend.utils.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Component
public class JwtTokenUtils {
    @Autowired
    private ObjectMapper objectMapper;

    private static final long EXPIRY_TIME = 432000000; // 5 days
    private static final Key secret = MacProvider.generateKey();

    public String generateToken(LoginUserRequest userRequest) {
        HashMap<String, String> claims = objectMapper.convertValue(userRequest,
                new TypeReference<HashMap<String, String>>() {
                });

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRY_TIME))
                .signWith(secret, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            String username = this.retrieveUser(token);
            return username != null && username.length() > 0;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public String retrieveUser(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("username").toString();
        } catch (JwtException e) {
            System.err.println(e);
            return null;
        }
    }
}
