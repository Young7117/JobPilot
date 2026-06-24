package com.jobpilot.knowledge.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class KnowledgeRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String category;
    private List<String> tags;
    private String masteryLevel = "needs_review";

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getMasteryLevel() { return masteryLevel; }
    public void setMasteryLevel(String masteryLevel) { this.masteryLevel = masteryLevel; }
}
