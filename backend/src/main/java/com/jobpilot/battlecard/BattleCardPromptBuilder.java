package com.jobpilot.battlecard;

import com.jobpilot.job.JobPost;
import com.jobpilot.resume.Resume;
import org.springframework.stereotype.Component;

@Component
public class BattleCardPromptBuilder {
    public static final String PROMPT_VERSION = "battle-card-v1";

    public String build(Resume resume, JobPost jobPost) {
        return """
                你是 JobPilot AI 的岗位作战顾问。用户是应届生或初级求职者。
                请基于简历和岗位 JD 输出严格 JSON，不要输出 Markdown，不要添加解释。

                输出字段：
                {
                  "matchScore": 78,
                  "coreRequirements": [],
                  "skillBreakdown": [],
                  "matchedPoints": [],
                  "weakPoints": [],
                  "resumeSuggestions": [],
                  "interviewFocus": [],
                  "threeDayPlan": [],
                  "sevenDayPlan": [],
                  "riskTips": []
                }

                简历：
                %s

                岗位 JD：
                %s
                """.formatted(resume.getContent(), jobPost.getJdContent());
    }
}
