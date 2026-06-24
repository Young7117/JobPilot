package com.jobpilot.resume.dto;

import com.jobpilot.resume.Resume;
import java.time.LocalDateTime;

public class ResumeResponse {
    private Long id;
    private String title;
    private String targetRole;
    private String content;
    private String fileType;
    private Integer version;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResumeResponse from(Resume resume) {
        ResumeResponse response = new ResumeResponse();
        response.id = resume.getId();
        response.title = resume.getTitle();
        response.targetRole = resume.getTargetRole();
        response.content = resume.getContent();
        response.fileType = resume.getFileType();
        response.version = resume.getVersion();
        response.isDefault = resume.getIsDefault();
        response.createdAt = resume.getCreatedAt();
        response.updatedAt = resume.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public String getContent() {
        return content;
    }

    public String getFileType() {
        return fileType;
    }

    public Integer getVersion() {
        return version;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
