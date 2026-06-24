package com.jobpilot.auth;

import com.jobpilot.auth.dto.AuthResponse;
import com.jobpilot.auth.dto.LoginRequest;
import com.jobpilot.auth.dto.RegisterRequest;
import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.common.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        authService.logout(SecurityUtils.currentUser().getTokenId());
        return ApiResponse.ok(null);
    }
}
