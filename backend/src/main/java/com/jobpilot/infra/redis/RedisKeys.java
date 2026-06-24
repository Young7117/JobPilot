package com.jobpilot.infra.redis;

public final class RedisKeys {
    private RedisKeys() {
    }

    public static String jwtBlacklist(String tokenId) {
        return "jwt:blacklist:" + tokenId;
    }

    public static String battleCardCache(String requestHash) {
        return "ai:battle-card:" + requestHash;
    }

    public static String questionGenerateCache(Long battleCardId, String promptVersion) {
        return "ai:question-generate:" + battleCardId + ":" + promptVersion;
    }

    public static String aiRateLimit(Long userId) {
        return "rate-limit:user:" + userId + ":ai-generate";
    }
}
