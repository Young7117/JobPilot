package com.jobpilot.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jobpilot.auth.dto.AuthResponse;
import com.jobpilot.auth.dto.LoginRequest;
import com.jobpilot.auth.dto.RegisterRequest;
import com.jobpilot.common.error.ConflictException;
import com.jobpilot.common.error.UnauthorizedException;
import com.jobpilot.config.properties.JwtProperties;
import com.jobpilot.infra.redis.RedisKeys;
import com.jobpilot.user.User;
import com.jobpilot.user.UserMapper;
import com.jobpilot.user.dto.UserProfileResponse;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthService(
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JwtProperties jwtProperties,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        requireUniqueUsername(request.getUsername());
        requireUniqueEmail(request.getEmail());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userMapper.insert(user);

        return authResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = findByAccount(request.getAccount());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid account or password");
        }
        return authResponse(user);
    }

    public void logout(String tokenId) {
        redisTemplate.opsForValue().set(
                RedisKeys.jwtBlacklist(tokenId),
                true,
                Duration.ofMinutes(jwtProperties.getExpirationMinutes())
        );
    }

    private AuthResponse authResponse(User user) {
        return new AuthResponse(jwtService.createToken(user), UserProfileResponse.from(user));
    }

    private void requireUniqueUsername(String username) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (count != null && count > 0) {
            throw new ConflictException("Username already exists");
        }
    }

    private void requireUniqueEmail(String email) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (count != null && count > 0) {
            throw new ConflictException("Email already exists");
        }
    }

    private User findByAccount(String account) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, account)
                .or()
                .eq(User::getEmail, account)
                .last("LIMIT 1"));
    }
}
