package com.jobpilot.auth;

import com.jobpilot.config.properties.JwtProperties;
import com.jobpilot.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtProperties properties;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
    }

    public String createToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(properties.getExpirationMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("uid", user.getId())
                .claim("email", user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey())
                .compact();
    }

    public JwtClaims parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Number userId = claims.get("uid", Number.class);
        String email = claims.get("email", String.class);
        return new JwtClaims(userId.longValue(), claims.getSubject(), email, claims.getId());
    }

    private SecretKey signingKey() {
        byte[] keyBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
