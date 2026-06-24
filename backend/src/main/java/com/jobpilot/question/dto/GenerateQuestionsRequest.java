package com.jobpilot.question.dto;

import jakarta.validation.constraints.NotNull;

public class GenerateQuestionsRequest {
    @NotNull
    private Long jobPostId;
    @NotNull
    private Long battleCardId;
    private Integer targetCount = 8;

    public Long getJobPostId() { return jobPostId; }
    public void setJobPostId(Long jobPostId) { this.jobPostId = jobPostId; }
    public Long getBattleCardId() { return battleCardId; }
    public void setBattleCardId(Long battleCardId) { this.battleCardId = battleCardId; }
    public Integer getTargetCount() { return targetCount; }
    public void setTargetCount(Integer targetCount) { this.targetCount = targetCount; }
}
