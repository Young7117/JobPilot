package com.jobpilot.application.dto;

import com.jobpilot.application.ApplicationRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ApplicationRecordResponse {
    private Long id;
    private Long jobPostId;
    private Long resumeId;
    private Long battleCardId;
    private String status;
    private LocalDate applyDate;
    private LocalDateTime interviewDate;
    private String result;
    private String failureReason;
    private String note;
    private List<String> aiReviewSuggestions;

    public static ApplicationRecordResponse from(ApplicationRecord record) {
        ApplicationRecordResponse response = new ApplicationRecordResponse();
        response.id = record.getId();
        response.jobPostId = record.getJobPostId();
        response.resumeId = record.getResumeId();
        response.battleCardId = record.getBattleCardId();
        response.status = record.getStatus();
        response.applyDate = record.getApplyDate();
        response.interviewDate = record.getInterviewDate();
        response.result = record.getResult();
        response.failureReason = record.getFailureReason();
        response.note = record.getNote();
        response.aiReviewSuggestions = record.getAiReviewSuggestions();
        return response;
    }

    public Long getId() { return id; }
    public Long getJobPostId() { return jobPostId; }
    public Long getResumeId() { return resumeId; }
    public Long getBattleCardId() { return battleCardId; }
    public String getStatus() { return status; }
    public LocalDate getApplyDate() { return applyDate; }
    public LocalDateTime getInterviewDate() { return interviewDate; }
    public String getResult() { return result; }
    public String getFailureReason() { return failureReason; }
    public String getNote() { return note; }
    public List<String> getAiReviewSuggestions() { return aiReviewSuggestions; }
}
