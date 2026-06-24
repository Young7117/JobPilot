package com.jobpilot.question;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobpilot.ai.AiCallLogService;
import com.jobpilot.ai.AiProvider;
import com.jobpilot.ai.AiRequest;
import com.jobpilot.ai.AiResponse;
import com.jobpilot.battlecard.BattleCard;
import com.jobpilot.battlecard.BattleCardService;
import com.jobpilot.common.error.BusinessException;
import com.jobpilot.common.error.NotFoundException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.common.security.UserScope;
import com.jobpilot.config.properties.AiProperties;
import com.jobpilot.job.JobPostService;
import com.jobpilot.question.dto.AiQuestionResult;
import com.jobpilot.question.dto.AiQuestionResult.GeneratedQuestion;
import com.jobpilot.question.dto.GenerateQuestionsRequest;
import com.jobpilot.question.dto.QuestionResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionWorkflowService {
    private final QuestionBankMapper questionBankMapper;
    private final CandidateQuestionMapper candidateQuestionMapper;
    private final UserQuestionMapper userQuestionMapper;
    private final BattleCardService battleCardService;
    private final JobPostService jobPostService;
    private final QuestionQueryBuilder queryBuilder;
    private final QuestionRetrievalService retrievalService;
    private final DuplicateQuestionService duplicateQuestionService;
    private final AiProvider aiProvider;
    private final AiProperties aiProperties;
    private final AiCallLogService aiCallLogService;
    private final ObjectMapper objectMapper;
    private final UserScope userScope;

    public QuestionWorkflowService(
            QuestionBankMapper questionBankMapper,
            CandidateQuestionMapper candidateQuestionMapper,
            UserQuestionMapper userQuestionMapper,
            BattleCardService battleCardService,
            JobPostService jobPostService,
            QuestionQueryBuilder queryBuilder,
            QuestionRetrievalService retrievalService,
            DuplicateQuestionService duplicateQuestionService,
            AiProvider aiProvider,
            AiProperties aiProperties,
            AiCallLogService aiCallLogService,
            ObjectMapper objectMapper,
            UserScope userScope
    ) {
        this.questionBankMapper = questionBankMapper;
        this.candidateQuestionMapper = candidateQuestionMapper;
        this.userQuestionMapper = userQuestionMapper;
        this.battleCardService = battleCardService;
        this.jobPostService = jobPostService;
        this.queryBuilder = queryBuilder;
        this.retrievalService = retrievalService;
        this.duplicateQuestionService = duplicateQuestionService;
        this.aiProvider = aiProvider;
        this.aiProperties = aiProperties;
        this.aiCallLogService = aiCallLogService;
        this.objectMapper = objectMapper;
        this.userScope = userScope;
    }

    @Transactional
    public List<QuestionResponse> generateForJob(GenerateQuestionsRequest request) {
        Long userId = SecurityUtils.currentUserId();
        jobPostService.requireMine(request.getJobPostId());
        BattleCard battleCard = battleCardService.requireMine(request.getBattleCardId());
        int targetCount = request.getTargetCount() == null ? 8 : request.getTargetCount();

        List<QuestionBank> retrieved = retrievalService.retrieve(queryBuilder.build(battleCard), targetCount);
        List<UserQuestion> assigned = new ArrayList<>();
        retrieved.forEach(question -> assigned.add(assignPublic(userId, request, question)));

        if (retrieved.size() < targetCount) {
            List<CandidateQuestion> generated = generateCandidates(userId, battleCard, targetCount - retrieved.size());
            generated.forEach(question -> assigned.add(assignCandidate(userId, request, question)));
        }

        return assigned.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<QuestionResponse> listMine() {
        Long userId = SecurityUtils.currentUserId();
        return userQuestionMapper.selectList(new LambdaQueryWrapper<UserQuestion>()
                        .eq(UserQuestion::getUserId, userId)
                        .isNotNull(UserQuestion::getQuestionId)
                        .orderByDesc(UserQuestion::getCreatedAt))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public QuestionResponse getMine(Long userQuestionId) {
        return toResponse(requireMine(userQuestionId));
    }

    public UserQuestion requireMine(Long userQuestionId) {
        UserQuestion userQuestion = userQuestionMapper.selectById(userQuestionId);
        if (userQuestion == null) {
            throw new NotFoundException("Question not found");
        }
        userScope.requireOwner(SecurityUtils.currentUserId(), userQuestion.getUserId());
        return userQuestion;
    }

    public QuestionResponse updateStatus(Long userQuestionId, String status) {
        UserQuestion userQuestion = requireMine(userQuestionId);
        userQuestion.setStatus(status);
        userQuestionMapper.updateById(userQuestion);
        return toResponse(userQuestion);
    }

    private UserQuestion assignPublic(Long userId, GenerateQuestionsRequest request, QuestionBank question) {
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setUserId(userId);
        userQuestion.setQuestionId(question.getId());
        userQuestion.setJobPostId(request.getJobPostId());
        userQuestion.setBattleCardId(request.getBattleCardId());
        userQuestion.setStatus("not_practiced");
        userQuestion.setSourceType("public");
        userQuestionMapper.insert(userQuestion);
        return userQuestion;
    }

    private UserQuestion assignCandidate(Long userId, GenerateQuestionsRequest request, CandidateQuestion question) {
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setUserId(userId);
        userQuestion.setCandidateQuestionId(question.getId());
        userQuestion.setJobPostId(request.getJobPostId());
        userQuestion.setBattleCardId(request.getBattleCardId());
        userQuestion.setStatus("not_practiced");
        userQuestion.setSourceType("candidate");
        userQuestionMapper.insert(userQuestion);
        return userQuestion;
    }

    private List<CandidateQuestion> generateCandidates(Long userId, BattleCard battleCard, int missingCount) {
        String prompt = "根据岗位作战卡短板补充 " + missingCount + " 道面试题：" + String.join(",", battleCard.getWeakPoints());
        AiResponse response = aiProvider.chat(new AiRequest("question-generate", prompt, aiProperties.getModel(), null));
        aiCallLogService.log(userId, "question-generate", null, response);
        AiQuestionResult result = parseQuestions(response.getContent());
        return result.getQuestions().stream().limit(missingCount).map(question -> saveCandidate(question, response.getContent())).toList();
    }

    private CandidateQuestion saveCandidate(GeneratedQuestion generated, String rawJson) {
        CandidateQuestion candidate = new CandidateQuestion();
        candidate.setTitle(generated.getTitle());
        candidate.setContent(generated.getContent());
        candidate.setQuestionType(generated.getType());
        candidate.setDifficulty(generated.getDifficulty());
        candidate.setTags(generated.getTags());
        candidate.setReferenceAnswer(generated.getReferenceAnswer());
        candidate.setQualityScore(BigDecimal.valueOf(85));
        candidate.setDuplicateScore(duplicateQuestionService.maxDuplicateScore(generated.getTitle(), generated.getContent()));
        candidate.setStatus("pending");
        candidate.setSource("ai");
        candidate.setRawAiResult(rawJson);
        candidateQuestionMapper.insert(candidate);
        return candidate;
    }

    private AiQuestionResult parseQuestions(String json) {
        try {
            return objectMapper.readValue(json, AiQuestionResult.class);
        } catch (Exception exception) {
            throw new BusinessException(HttpStatus.BAD_GATEWAY, "AI question response is not valid JSON");
        }
    }

    public QuestionResponse toResponse(UserQuestion item) {
        if (item.getQuestionId() != null) {
            return QuestionResponse.fromPublic(item, questionBankMapper.selectById(item.getQuestionId()));
        }
        return QuestionResponse.fromCandidate(item, candidateQuestionMapper.selectById(item.getCandidateQuestionId()));
    }
}
