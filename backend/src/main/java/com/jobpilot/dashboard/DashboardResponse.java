package com.jobpilot.dashboard;

import java.util.Map;

public class DashboardResponse {
    private long resumeCount;
    private long jobCount;
    private long battleCardCount;
    private long questionCount;
    private long reviewQueueCount;
    private Map<String, Long> applicationStatusStats;

    public long getResumeCount() { return resumeCount; }
    public void setResumeCount(long resumeCount) { this.resumeCount = resumeCount; }
    public long getJobCount() { return jobCount; }
    public void setJobCount(long jobCount) { this.jobCount = jobCount; }
    public long getBattleCardCount() { return battleCardCount; }
    public void setBattleCardCount(long battleCardCount) { this.battleCardCount = battleCardCount; }
    public long getQuestionCount() { return questionCount; }
    public void setQuestionCount(long questionCount) { this.questionCount = questionCount; }
    public long getReviewQueueCount() { return reviewQueueCount; }
    public void setReviewQueueCount(long reviewQueueCount) { this.reviewQueueCount = reviewQueueCount; }
    public Map<String, Long> getApplicationStatusStats() { return applicationStatusStats; }
    public void setApplicationStatusStats(Map<String, Long> applicationStatusStats) { this.applicationStatusStats = applicationStatusStats; }
}
