package com.jobpilot.common.security;

public class CurrentUser {
    private final Long id;
    private final String username;
    private final String email;
    private final String tokenId;

    public CurrentUser(Long id, String username, String email, String tokenId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.tokenId = tokenId;
    }

    public Long getId() {
        return id;
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
