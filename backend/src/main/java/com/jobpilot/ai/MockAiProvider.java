package com.jobpilot.ai;

import com.jobpilot.config.properties.AiProperties;
import org.springframework.stereotype.Component;

@Component
public class MockAiProvider implements AiProvider {
    private final AiProperties properties;

    public MockAiProvider(AiProperties properties) {
        this.properties = properties;
    }

    @Override
    public AiResponse chat(AiRequest request) {
        if ("battle-card".equals(request.getScene())) {
            return new AiResponse(mockBattleCard(), 1200, 800, properties.getModel(), 0.0);
        }
        if ("question-generate".equals(request.getScene())) {
            return new AiResponse(mockQuestions(), 900, 700, properties.getModel(), 0.0);
        }
        if ("answer-evaluate".equals(request.getScene())) {
            return new AiResponse(mockEvaluation(), 600, 500, properties.getModel(), 0.0);
        }
        if ("application-review".equals(request.getScene())) {
            return new AiResponse(mockApplicationReview(), 500, 400, properties.getModel(), 0.0);
        }
        return new AiResponse("{}", 0, 0, properties.getModel(), 0.0);
    }

    private String mockBattleCard() {
        return """
                {
                  "matchScore": 78,
                  "coreRequirements": ["Spring Boot 后端开发", "MySQL 与 Redis 基础", "接口设计与部署意识"],
                  "skillBreakdown": ["Java 17", "Spring Boot", "MySQL", "Redis", "Docker", "项目表达"],
                  "matchedPoints": ["已有 Web 项目经历", "具备数据库和缓存基础", "能围绕岗位 JD 做针对性准备"],
                  "weakPoints": ["部署链路表达需要补强", "高并发场景回答需要结构化", "简历项目指标还不够具体"],
                  "resumeSuggestions": ["补充项目中的量化结果", "把技术栈写到项目描述前两行", "突出与 JD 对应的职责和产出"],
                  "interviewFocus": ["项目架构讲解", "Redis 缓存问题", "MySQL 索引优化", "Docker 部署流程"],
                  "threeDayPlan": ["第 1 天梳理项目链路", "第 2 天补 Redis/MySQL 高频题", "第 3 天模拟项目追问"],
                  "sevenDayPlan": ["完善简历项目指标", "完成 30 道岗位相关题", "准备 2 套项目讲解话术", "复盘投递风险"],
                  "riskTips": ["如果 JD 强调生产经验，需要提前准备部署和排障案例", "避免只背八股，必须绑定自己的项目回答"]
                }
                """;
    }

    private String mockQuestions() {
        return """
                {
                  "questions": [
                    {
                      "type": "Redis 专项题",
                      "difficulty": "中等",
                      "title": "如何结合项目解决缓存穿透？",
                      "content": "请说明缓存穿透产生原因，并结合你的项目说明布隆过滤器、空值缓存和参数校验如何配合。",
                      "referenceAnswer": "缓存穿透是请求不存在的数据绕过缓存打到数据库。可以用参数校验拦截非法请求，布隆过滤器过滤不存在 key，对确认为空的数据短期缓存空值。",
                      "tags": ["Redis", "缓存穿透", "项目追问"]
                    }
                  ]
                }
                """;
    }

    private String mockEvaluation() {
        return """
                {
                  "score": 82,
                  "feedback": "回答覆盖了核心概念，但项目细节和量化结果还可以更具体。",
                  "optimizedAnswer": "我会先解释问题产生的原因，再结合项目中的缓存结构说明解决方案，最后补充监控和降级手段。",
                  "followUp": ["如果热点 key 同时失效怎么办？", "如何验证优化后数据库压力下降？"]
                }
                """;
    }

    private String mockApplicationReview() {
        return """
                {
                  "suggestions": [
                    "复盘 JD 中未覆盖的能力点，优先补充到下一版作战卡。",
                    "如果失败原因集中在项目深度，下一轮准备时增加项目追问训练。",
                    "把本次面试中的高频问题沉淀到个人知识库并标记为高频重点。"
                  ]
                }
                """;
    }
}
