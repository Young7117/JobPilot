package com.jobpilot.application.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationRecordRequest {
    @NotNull
    private Long jobPostId;
    private Long resumeId;
    private Long battleCardId;
    private String status = "planned";
    private LocalDate applyDate;
    private LocalDateTime interviewDate;
    private String result;
    private String failureReason;
    private String note;

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
}
