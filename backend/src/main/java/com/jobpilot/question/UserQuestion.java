package com.jobpilot.question;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("user_question")
public class UserQuestion {
    @TableId
    private Long id;
    private Long userId;
    private Long questionId;
    private Long candidateQuestionId;
    private Long jobPostId;
    private Long battleCardId;
    private String status;
    private String sourceType;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Long getCandidateQuestionId() { return candidateQuestionId; }
    public void setCandidateQuestionId(Long candidateQuestionId) { this.candidateQuestionId = candidateQuestionId; }
    public Long getJobPostId() { return jobPostId; }
    public void setJobPostId(Long jobPostId) { this.jobPostId = jobPostId; }
    public Long getBattleCardId() { return battleCardId; }
    public void setBattleCardId(Long battleCardId) { this.battleCardId = battleCardId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
