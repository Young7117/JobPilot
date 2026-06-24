package com.jobpilot.battlecard.dto;

import java.util.List;

public class BattleCardAiResult {
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

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }
    public List<String> getCoreRequirements() { return coreRequirements; }
    public void setCoreRequirements(List<String> coreRequirements) { this.coreRequirements = coreRequirements; }
    public List<String> getSkillBreakdown() { return skillBreakdown; }
    public void setSkillBreakdown(List<String> skillBreakdown) { this.skillBreakdown = skillBreakdown; }
    public List<String> getMatchedPoints() { return matchedPoints; }
    public void setMatchedPoints(List<String> matchedPoints) { this.matchedPoints = matchedPoints; }
    public List<String> getWeakPoints() { return weakPoints; }
    public void setWeakPoints(List<String> weakPoints) { this.weakPoints = weakPoints; }
    public List<String> getResumeSuggestions() { return resumeSuggestions; }
    public void setResumeSuggestions(List<String> resumeSuggestions) { this.resumeSuggestions = resumeSuggestions; }
    public List<String> getInterviewFocus() { return interviewFocus; }
    public void setInterviewFocus(List<String> interviewFocus) { this.interviewFocus = interviewFocus; }
    public List<String> getThreeDayPlan() { return threeDayPlan; }
    public void setThreeDayPlan(List<String> threeDayPlan) { this.threeDayPlan = threeDayPlan; }
    public List<String> getSevenDayPlan() { return sevenDayPlan; }
    public void setSevenDayPlan(List<String> sevenDayPlan) { this.sevenDayPlan = sevenDayPlan; }
    public List<String> getRiskTips() { return riskTips; }
    public void setRiskTips(List<String> riskTips) { this.riskTips = riskTips; }
}
