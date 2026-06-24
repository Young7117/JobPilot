package com.jobpilot.practice;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.time.LocalDateTime;
import java.util.List;

@TableName(value = "question_practice", autoResultMap = true)
public class QuestionPractice {
    @TableId
    private Long id;
    private Long userId;
    private Long userQuestionId;
    private String userAnswer;
    private Integer aiScore;
    private String aiFeedback;
    private String aiOptimizedAnswer;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> aiFollowUp;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getUserQuestionId() { return userQuestionId; }
    public void setUserQuestionId(Long userQuestionId) { this.userQuestionId = userQuestionId; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public Integer getAiScore() { return aiScore; }
    public void setAiScore(Integer aiScore) { this.aiScore = aiScore; }
    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }
    public String getAiOptimizedAnswer() { return aiOptimizedAnswer; }
    public void setAiOptimizedAnswer(String aiOptimizedAnswer) { this.aiOptimizedAnswer = aiOptimizedAnswer; }
    public List<String> getAiFollowUp() { return aiFollowUp; }
    public void setAiFollowUp(List<String> aiFollowUp) { this.aiFollowUp = aiFollowUp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
