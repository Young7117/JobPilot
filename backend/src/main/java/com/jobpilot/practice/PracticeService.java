package com.jobpilot.practice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobpilot.ai.AiCallLogService;
import com.jobpilot.ai.AiProvider;
import com.jobpilot.ai.AiRequest;
import com.jobpilot.ai.AiResponse;
import com.jobpilot.common.error.BusinessException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.config.properties.AiProperties;
import com.jobpilot.practice.dto.EvaluationResult;
import com.jobpilot.practice.dto.PracticeAnswerRequest;
import com.jobpilot.practice.dto.PracticeResponse;
import com.jobpilot.question.QuestionWorkflowService;
import com.jobpilot.question.UserQuestion;
import com.jobpilot.question.dto.QuestionResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PracticeService {
    private final QuestionPracticeMapper practiceMapper;
    private final QuestionWorkflowService questionWorkflowService;
    private final AiProvider aiProvider;
    private final AiProperties aiProperties;
    private final AiCallLogService aiCallLogService;
    private final ObjectMapper objectMapper;

    public PracticeService(
            QuestionPracticeMapper practiceMapper,
            QuestionWorkflowService questionWorkflowService,
            AiProvider aiProvider,
            AiProperties aiProperties,
            AiCallLogService aiCallLogService,
            ObjectMapper objectMapper
    ) {
        this.practiceMapper = practiceMapper;
        this.questionWorkflowService = questionWorkflowService;
        this.aiProvider = aiProvider;
        this.aiProperties = aiProperties;
        this.aiCallLogService = aiCallLogService;
        this.objectMapper = objectMapper;
    }

    public PracticeResponse submit(Long userQuestionId, PracticeAnswerRequest request) {
        UserQuestion userQuestion = questionWorkflowService.requireMine(userQuestionId);
        QuestionPractice practice = basePractice(userQuestion, request.getUserAnswer());
        practiceMapper.insert(practice);
        return PracticeResponse.from(practice);
    }

    public PracticeResponse evaluate(Long userQuestionId, PracticeAnswerRequest request) {
        Long userId = SecurityUtils.currentUserId();
        UserQuestion userQuestion = questionWorkflowService.requireMine(userQuestionId);
        QuestionResponse question = questionWorkflowService.toResponse(userQuestion);
        String prompt = """
                题目：%s
                参考答案：%s
                用户答案：%s
                请输出 JSON：score, feedback, optimizedAnswer, followUp。
                """.formatted(question.getContent(), question.getReferenceAnswer(), request.getUserAnswer());
        AiResponse response = aiProvider.chat(new AiRequest("answer-evaluate", prompt, aiProperties.getModel(), null));
        aiCallLogService.log(userId, "answer-evaluate", null, response);
        EvaluationResult result = parse(response.getContent());

        QuestionPractice practice = basePractice(userQuestion, request.getUserAnswer());
        practice.setAiScore(result.getScore());
        practice.setAiFeedback(result.getFeedback());
        practice.setAiOptimizedAnswer(result.getOptimizedAnswer());
        practice.setAiFollowUp(result.getFollowUp());
        practiceMapper.insert(practice);
        return PracticeResponse.from(practice);
    }

    public List<PracticeResponse> history(Long userQuestionId) {
        UserQuestion userQuestion = questionWorkflowService.requireMine(userQuestionId);
        return practiceMapper.selectList(new LambdaQueryWrapper<QuestionPractice>()
                        .eq(QuestionPractice::getUserId, userQuestion.getUserId())
                        .eq(QuestionPractice::getUserQuestionId, userQuestionId)
                        .orderByDesc(QuestionPractice::getCreatedAt))
                .stream()
                .map(PracticeResponse::from)
                .toList();
    }

    private QuestionPractice basePractice(UserQuestion userQuestion, String answer) {
        QuestionPractice practice = new QuestionPractice();
        practice.setUserId(userQuestion.getUserId());
        practice.setUserQuestionId(userQuestion.getId());
        practice.setUserAnswer(answer);
        return practice;
    }

    private EvaluationResult parse(String json) {
        try {
            return objectMapper.readValue(json, EvaluationResult.class);
        } catch (Exception exception) {
            throw new BusinessException(HttpStatus.BAD_GATEWAY, "AI evaluation response is not valid JSON");
        }
    }
}
