package com.jobpilot.ai;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class AiCallLogService {
    private final AiCallLogMapper mapper;

    public AiCallLogService(AiCallLogMapper mapper) {
        this.mapper = mapper;
    }

    public void log(Long userId, String scene, String requestHash, AiResponse response) {
        AiCallLog log = new AiCallLog();
        log.setUserId(userId);
        log.setScene(scene);
        log.setRequestHash(requestHash);
        log.setPromptTokens(response.getPromptTokens());
        log.setCompletionTokens(response.getCompletionTokens());
        log.setModelName(response.getModelName());
        log.setCost(response.getCost() == null ? null : BigDecimal.valueOf(response.getCost()));
        mapper.insert(log);
    }
}
