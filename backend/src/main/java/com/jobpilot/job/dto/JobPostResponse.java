package com.jobpilot.job.dto;

import com.jobpilot.job.JobPost;
import java.time.LocalDateTime;

public class JobPostResponse {
    private Long id;
    private String companyName;
    private String positionName;
    private String industry;
    private String city;
    private String salaryRange;
    private String jobDirection;
    private String jdContent;
    private String source;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static JobPostResponse from(JobPost jobPost) {
        JobPostResponse response = new JobPostResponse();
        response.id = jobPost.getId();
        response.companyName = jobPost.getCompanyName();
        response.positionName = jobPost.getPositionName();
        response.industry = jobPost.getIndustry();
        response.city = jobPost.getCity();
        response.salaryRange = jobPost.getSalaryRange();
        response.jobDirection = jobPost.getJobDirection();
        response.jdContent = jobPost.getJdContent();
        response.source = jobPost.getSource();
        response.status = jobPost.getStatus();
        response.createdAt = jobPost.getCreatedAt();
        response.updatedAt = jobPost.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getIndustry() {
        return industry;
    }

    public String getCity() {
        return city;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getJobDirection() {
        return jobDirection;
    }

    public String getJdContent() {
        return jdContent;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
