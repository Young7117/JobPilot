package com.jobpilot.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JobPostRequest {
    @NotBlank
    @Size(max = 128)
    private String companyName;
    @NotBlank
    @Size(max = 128)
    private String positionName;
    @Size(max = 96)
    private String industry = "计算机/互联网";
    @Size(max = 96)
    private String city;
    @Size(max = 96)
    private String salaryRange;
    @Size(max = 96)
    private String jobDirection;
    @NotBlank
    private String jdContent;
    @Size(max = 128)
    private String source;
    @Size(max = 32)
    private String status = "preparing";

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getJobDirection() {
        return jobDirection;
    }

    public void setJobDirection(String jobDirection) {
        this.jobDirection = jobDirection;
    }

    public String getJdContent() {
        return jdContent;
    }

    public void setJdContent(String jdContent) {
        this.jdContent = jdContent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
