package com.jobpilot.battlecard.dto;

import jakarta.validation.constraints.NotNull;

public class BattleCardGenerateRequest {
    @NotNull
    private Long resumeId;
    @NotNull
    private Long jobPostId;

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public Long getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Long jobPostId) {
        this.jobPostId = jobPostId;
    }
}
