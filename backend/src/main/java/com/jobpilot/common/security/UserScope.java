package com.jobpilot.common.security;

import com.jobpilot.common.error.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class UserScope {

    public void requireOwner(Long authenticatedUserId, Long ownerUserId) {
        if (authenticatedUserId == null || ownerUserId == null || !authenticatedUserId.equals(ownerUserId)) {
            throw new ForbiddenException("You do not have access to this resource");
        }
    }
}
