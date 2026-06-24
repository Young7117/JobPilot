package com.jobpilot.resume;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResumeJobIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        when(redisTemplate.hasKey(startsWith("jwt:blacklist:"))).thenReturn(false);
    }

    @Test
    void resumeAccessIsUserScoped() throws Exception {
        String ownerToken = register("resume_owner");
        String otherToken = register("resume_other");

        long resumeId = createResume(ownerToken);

        mockMvc.perform(get("/api/resumes/{id}", resumeId)
                        .header("Authorization", bearer(ownerToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("后端开发简历"));

        mockMvc.perform(get("/api/resumes/{id}", resumeId)
                        .header("Authorization", bearer(otherToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void jobPostAccessIsUserScoped() throws Exception {
        String ownerToken = register("job_owner");
        String otherToken = register("job_other");

        long jobId = createJob(ownerToken);

        mockMvc.perform(get("/api/jobs/{id}", jobId)
                        .header("Authorization", bearer(ownerToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.companyName").value("JobPilot Demo"));

        mockMvc.perform(get("/api/jobs/{id}", jobId)
                        .header("Authorization", bearer(otherToken)))
                .andExpect(status().isForbidden());
    }

    private long createResume(String token) throws Exception {
        String response = mockMvc.perform(post("/api/resumes/text")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "后端开发简历",
                                  "targetRole": "Java 后端",
                                  "content": "Java Spring Boot Redis MySQL 项目经历"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).path("data").path("id").asLong();
    }

    private long createJob(String token) throws Exception {
        String response = mockMvc.perform(post("/api/jobs")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "companyName": "JobPilot Demo",
                                  "positionName": "Java 后端开发",
                                  "industry": "计算机/互联网",
                                  "jobDirection": "Java 后端",
                                  "jdContent": "熟悉 Spring Boot、MySQL、Redis 和 Docker",
                                  "status": "preparing"
                                }
                                """))
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
        JsonNode json = objectMapper.readTree(response);
        return json.path("data").path("token").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
