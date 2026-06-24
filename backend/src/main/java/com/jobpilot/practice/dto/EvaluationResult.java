package com.jobpilot.practice.dto;

import java.util.List;

public class EvaluationResult {
    private Integer score;
    private String feedback;
    private String optimizedAnswer;
    private List<String> followUp;

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public String getOptimizedAnswer() { return optimizedAnswer; }
    public void setOptimizedAnswer(String optimizedAnswer) { this.optimizedAnswer = optimizedAnswer; }
    public List<String> getFollowUp() { return followUp; }
    public void setFollowUp(List<String> followUp) { this.followUp = followUp; }
}
