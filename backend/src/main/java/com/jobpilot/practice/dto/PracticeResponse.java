package com.jobpilot.practice.dto;

import com.jobpilot.practice.QuestionPractice;
import java.time.LocalDateTime;
import java.util.List;

public class PracticeResponse {
    private Long id;
    private Long userQuestionId;
    private String userAnswer;
    private Integer aiScore;
    private String aiFeedback;
    private String aiOptimizedAnswer;
    private List<String> aiFollowUp;
    private LocalDateTime createdAt;

    public static PracticeResponse from(QuestionPractice practice) {
        PracticeResponse response = new PracticeResponse();
        response.id = practice.getId();
        response.userQuestionId = practice.getUserQuestionId();
        response.userAnswer = practice.getUserAnswer();
        response.aiScore = practice.getAiScore();
        response.aiFeedback = practice.getAiFeedback();
        response.aiOptimizedAnswer = practice.getAiOptimizedAnswer();
        response.aiFollowUp = practice.getAiFollowUp();
        response.createdAt = practice.getCreatedAt();
        return response;
    }

    public Long getId() { return id; }
    public Long getUserQuestionId() { return userQuestionId; }
    public String getUserAnswer() { return userAnswer; }
    public Integer getAiScore() { return aiScore; }
    public String getAiFeedback() { return aiFeedback; }
    public String getAiOptimizedAnswer() { return aiOptimizedAnswer; }
    public List<String> getAiFollowUp() { return aiFollowUp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
