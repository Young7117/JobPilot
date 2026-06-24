package com.jobpilot.user;

import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.common.error.NotFoundException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.user.dto.UserProfileResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me() {
        User user = userMapper.selectById(SecurityUtils.currentUserId());
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return ApiResponse.ok(UserProfileResponse.from(user));
    }
}
