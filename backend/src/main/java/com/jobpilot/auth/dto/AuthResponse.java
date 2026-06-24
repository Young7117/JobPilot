package com.jobpilot.auth.dto;

import com.jobpilot.user.dto.UserProfileResponse;

public class AuthResponse {
    private String token;
    private UserProfileResponse user;

    public AuthResponse(String token, UserProfileResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserProfileResponse getUser() {
        return user;
    }
}
