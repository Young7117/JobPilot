package com.jobpilot.knowledge.dto;

import com.jobpilot.knowledge.PersonalKnowledgeItem;
import java.time.LocalDateTime;
import java.util.List;

public class KnowledgeResponse {
    private Long id;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String sourceType;
    private Long sourceId;
    private Long sourceQuestionId;
    private String masteryLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static KnowledgeResponse from(PersonalKnowledgeItem item) {
        KnowledgeResponse response = new KnowledgeResponse();
        response.id = item.getId();
        response.title = item.getTitle();
        response.content = item.getContent();
        response.category = item.getCategory();
        response.tags = item.getTags();
        response.sourceType = item.getSourceType();
        response.sourceId = item.getSourceId();
        response.sourceQuestionId = item.getSourceQuestionId();
        response.masteryLevel = item.getMasteryLevel();
        response.createdAt = item.getCreatedAt();
        response.updatedAt = item.getUpdatedAt();
        return response;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public List<String> getTags() { return tags; }
    public String getSourceType() { return sourceType; }
    public Long getSourceId() { return sourceId; }
    public Long getSourceQuestionId() { return sourceQuestionId; }
    public String getMasteryLevel() { return masteryLevel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
