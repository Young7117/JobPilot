package com.jobpilot.battlecard;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.time.LocalDateTime;
import java.util.List;

@TableName(value = "battle_card", autoResultMap = true)
public class BattleCard {
    @TableId
    private Long id;
    private Long userId;
    private Long resumeId;
    private Long jobPostId;
    private Integer matchScore;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> coreRequirements;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> skillBreakdown;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> matchedPoints;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> weakPoints;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> resumeSuggestions;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> interviewFocus;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> threeDayPlan;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> sevenDayPlan;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> riskTips;
    private String rawAiResult;
    private String requestHash;
    private String promptVersion;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }
    public Long getJobPostId() { return jobPostId; }
    public void setJobPostId(Long jobPostId) { this.jobPostId = jobPostId; }
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
    public String getRawAiResult() { return rawAiResult; }
    public void setRawAiResult(String rawAiResult) { this.rawAiResult = rawAiResult; }
    public String getRequestHash() { return requestHash; }
    public void setRequestHash(String requestHash) { this.requestHash = requestHash; }
    public String getPromptVersion() { return promptVersion; }
    public void setPromptVersion(String promptVersion) { this.promptVersion = promptVersion; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
