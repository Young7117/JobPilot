package com.jobpilot.auth;

public class JwtClaims {
    private final Long userId;
    private final String username;
    private final String email;
    private final String tokenId;

    public JwtClaims(Long userId, String username, String email, String tokenId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.tokenId = tokenId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getTokenId() {
        return tokenId;
    }
}
