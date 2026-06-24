package com.jobpilot.battlecard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobpilot.ai.AiProvider;
import com.jobpilot.ai.AiResponse;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BattleCardIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RedisTemplate<String, Object> redisTemplate;
    @MockBean
    private ValueOperations<String, Object> valueOperations;
    @MockBean
    private AiProvider aiProvider;

    @BeforeEach
    void setUp() {
        when(redisTemplate.hasKey(startsWith("jwt:blacklist:"))).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(startsWith("ai:battle-card:"))).thenReturn(null);
    }

    @Test
    void generateBattleCardPersistsAiResultAndLogsCall() throws Exception {
        when(aiProvider.chat(any())).thenReturn(new AiResponse(validBattleCardJson(), 10, 20, "mock", 0.0));
        String token = register("battle_success");
        long resumeId = createResume(token, "battle_success_resume");
        long jobId = createJob(token, "battle_success_job");

        mockMvc.perform(post("/api/battle-cards/generate")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "resumeId": %d,
                                  "jobPostId": %d
                                }
                                """.formatted(resumeId, jobId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.matchScore").value(86))
                .andExpect(jsonPath("$.data.coreRequirements[0]").value("Java 后端"));

        verify(valueOperations).set(startsWith("ai:battle-card:"), eq(validBattleCardJson()), any(Duration.class));
    }

    @Test
    void cacheHitSkipsAiProvider() throws Exception {
        when(valueOperations.get(startsWith("ai:battle-card:"))).thenReturn(validBattleCardJson());
        String token = register("battle_cache");
        long resumeId = createResume(token, "battle_cache_resume");
        long jobId = createJob(token, "battle_cache_job");

        mockMvc.perform(post("/api/battle-cards/generate")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "resumeId": %d,
                                  "jobPostId": %d
                                }
                                """.formatted(resumeId, jobId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.matchScore").value(86));

        verify(aiProvider, never()).chat(any());
    }

    @Test
    void malformedAiResponseReturnsBadGateway() throws Exception {
        when(aiProvider.chat(any())).thenReturn(new AiResponse("not-json", 1, 1, "mock", 0.0));
        String token = register("battle_malformed");
        long resumeId = createResume(token, "battle_malformed_resume");
        long jobId = createJob(token, "battle_malformed_job");

        mockMvc.perform(post("/api/battle-cards/generate")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "resumeId": %d,
                                  "jobPostId": %d
                                }
                                """.formatted(resumeId, jobId)))
                .andExpect(status().isBadGateway());
    }

    @Test
    void sourceOwnershipIsChecked() throws Exception {
        when(aiProvider.chat(any())).thenReturn(new AiResponse(validBattleCardJson(), 10, 20, "mock", 0.0));
        String ownerToken = register("battle_owner");
        String otherToken = register("battle_other");
        long ownerResumeId = createResume(ownerToken, "battle_owner_resume");
        long otherJobId = createJob(otherToken, "battle_other_job");

        mockMvc.perform(post("/api/battle-cards/generate")
                        .header("Authorization", bearer(ownerToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "resumeId": %d,
                                  "jobPostId": %d
                                }
                                """.formatted(ownerResumeId, otherJobId)))
                .andExpect(status().isForbidden());
    }

    private long createResume(String token, String title) throws Exception {
        String response = mockMvc.perform(post("/api/resumes/text")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "%s",
                                  "targetRole": "Java 后端",
                                  "content": "Java Spring Boot Redis MySQL 项目经历"
                                }
                                """.formatted(title)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).path("data").path("id").asLong();
    }

    private long createJob(String token, String companyName) throws Exception {
        String response = mockMvc.perform(post("/api/jobs")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "companyName": "%s",
                                  "positionName": "Java 后端开发",
                                  "industry": "计算机/互联网",
                                  "jobDirection": "Java 后端",
                                  "jdContent": "熟悉 Spring Boot、MySQL、Redis 和 Docker",
                                  "status": "preparing"
                                }
                                """.formatted(companyName)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).path("data").path("id").asLong();
    }

    private String register(String prefix) throws Exception {
        String response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "email": "%s@example.com",
                                  "password": "password123"
                                }
                                """.formatted(prefix, prefix)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).path("data").path("token").asText();
    }

    private String validBattleCardJson() {
        return """
                {
                  "matchScore": 86,
                  "coreRequirements": ["Java 后端"],
                  "skillBreakdown": ["Spring Boot", "Redis"],
                  "matchedPoints": ["项目经历匹配"],
                  "weakPoints": ["部署表达不足"],
                  "resumeSuggestions": ["补充量化结果"],
                  "interviewFocus": ["Redis 缓存"],
                  "threeDayPlan": ["复习 Redis"],
                  "sevenDayPlan": ["模拟面试"],
                  "riskTips": ["准备部署追问"]
                }
                """;
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
