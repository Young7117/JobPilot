package com.jobpilot.battlecard.dto;

import com.jobpilot.battlecard.BattleCard;
import java.time.LocalDateTime;
import java.util.List;

public class BattleCardResponse {
    private Long id;
    private Long resumeId;
    private Long jobPostId;
    private Integer matchScore;
    private List<String> coreRequirements;
    private List<String> skillBreakdown;
    private List<String> matchedPoints;
    private List<String> weakPoints;
    private List<String> resumeSuggestions;
    private List<String> interviewFocus;
    private List<String> threeDayPlan;
    private List<String> sevenDayPlan;
    private List<String> riskTips;
    private LocalDateTime createdAt;

    public static BattleCardResponse from(BattleCard card) {
        BattleCardResponse response = new BattleCardResponse();
        response.id = card.getId();
        response.resumeId = card.getResumeId();
        response.jobPostId = card.getJobPostId();
        response.matchScore = card.getMatchScore();
        response.coreRequirements = card.getCoreRequirements();
        response.skillBreakdown = card.getSkillBreakdown();
        response.matchedPoints = card.getMatchedPoints();
        response.weakPoints = card.getWeakPoints();
        response.resumeSuggestions = card.getResumeSuggestions();
        response.interviewFocus = card.getInterviewFocus();
        response.threeDayPlan = card.getThreeDayPlan();
        response.sevenDayPlan = card.getSevenDayPlan();
        response.riskTips = card.getRiskTips();
        response.createdAt = card.getCreatedAt();
        return response;
    }

    public Long getId() { return id; }
    public Long getResumeId() { return resumeId; }
    public Long getJobPostId() { return jobPostId; }
    public Integer getMatchScore() { return matchScore; }
    public List<String> getCoreRequirements() { return coreRequirements; }
    public List<String> getSkillBreakdown() { return skillBreakdown; }
    public List<String> getMatchedPoints() { return matchedPoints; }
    public List<String> getWeakPoints() { return weakPoints; }
    public List<String> getResumeSuggestions() { return resumeSuggestions; }
    public List<String> getInterviewFocus() { return interviewFocus; }
    public List<String> getThreeDayPlan() { return threeDayPlan; }
    public List<String> getSevenDayPlan() { return sevenDayPlan; }
    public List<String> getRiskTips() { return riskTips; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
