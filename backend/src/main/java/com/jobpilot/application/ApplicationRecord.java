package com.jobpilot.application;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@TableName(value = "application_record", autoResultMap = true)
public class ApplicationRecord {
    @TableId
    private Long id;
    private Long userId;
    private Long jobPostId;
    private Long resumeId;
    private Long battleCardId;
    private String status;
    private LocalDate applyDate;
    private LocalDateTime interviewDate;
    private String result;
    private String failureReason;
    private String note;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> aiReviewSuggestions;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getJobPostId() { return jobPostId; }
    public void setJobPostId(Long jobPostId) { this.jobPostId = jobPostId; }
    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }
    public Long getBattleCardId() { return battleCardId; }
    public void setBattleCardId(Long battleCardId) { this.battleCardId = battleCardId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getApplyDate() { return applyDate; }
    public void setApplyDate(LocalDate applyDate) { this.applyDate = applyDate; }
    public LocalDateTime getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDateTime interviewDate) { this.interviewDate = interviewDate; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<String> getAiReviewSuggestions() { return aiReviewSuggestions; }
    public void setAiReviewSuggestions(List<String> aiReviewSuggestions) { this.aiReviewSuggestions = aiReviewSuggestions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
