package com.jobpilot.battlecard;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobpilot.ai.AiCallLogService;
import com.jobpilot.ai.AiProvider;
import com.jobpilot.ai.AiRequest;
import com.jobpilot.ai.AiResponse;
import com.jobpilot.battlecard.dto.BattleCardAiResult;
import com.jobpilot.battlecard.dto.BattleCardGenerateRequest;
import com.jobpilot.battlecard.dto.BattleCardResponse;
import com.jobpilot.common.error.BusinessException;
import com.jobpilot.common.error.NotFoundException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.common.security.UserScope;
import com.jobpilot.common.util.Hashing;
import com.jobpilot.config.properties.AiProperties;
import com.jobpilot.infra.redis.RedisKeys;
import com.jobpilot.job.JobPost;
import com.jobpilot.job.JobPostService;
import com.jobpilot.resume.Resume;
import com.jobpilot.resume.ResumeService;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BattleCardService {
    private final BattleCardMapper battleCardMapper;
    private final ResumeService resumeService;
    private final JobPostService jobPostService;
    private final BattleCardPromptBuilder promptBuilder;
    private final AiProvider aiProvider;
    private final AiProperties aiProperties;
    private final AiCallLogService aiCallLogService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final UserScope userScope;

    public BattleCardService(
            BattleCardMapper battleCardMapper,
            ResumeService resumeService,
            JobPostService jobPostService,
            BattleCardPromptBuilder promptBuilder,
            AiProvider aiProvider,
            AiProperties aiProperties,
            AiCallLogService aiCallLogService,
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper,
            UserScope userScope
    ) {
        this.battleCardMapper = battleCardMapper;
        this.resumeService = resumeService;
        this.jobPostService = jobPostService;
        this.promptBuilder = promptBuilder;
        this.aiProvider = aiProvider;
        this.aiProperties = aiProperties;
        this.aiCallLogService = aiCallLogService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.userScope = userScope;
    }

    @Transactional
    public BattleCardResponse generate(BattleCardGenerateRequest request) {
        Long userId = SecurityUtils.currentUserId();
        Resume resume = resumeService.requireOwnedEntity(request.getResumeId());
        JobPost jobPost = jobPostService.requireMine(request.getJobPostId());
        String prompt = promptBuilder.build(resume, jobPost);
        String requestHash = Hashing.sha256(resume.getContent() + "\n" + jobPost.getJdContent() + "\n" + BattleCardPromptBuilder.PROMPT_VERSION);

        BattleCardAiResult result = getOrCreateAiResult(userId, prompt, requestHash);
        BattleCard card = toEntity(result, userId, resume, jobPost, requestHash);
        battleCardMapper.insert(card);
        return BattleCardResponse.from(card);
    }

    public BattleCardResponse getMine(Long id) {
        return BattleCardResponse.from(requireMine(id));
    }

    public BattleCardResponse getByJob(Long jobId) {
        Long userId = SecurityUtils.currentUserId();
        BattleCard card = battleCardMapper.selectOne(new LambdaQueryWrapper<BattleCard>()
                .eq(BattleCard::getUserId, userId)
                .eq(BattleCard::getJobPostId, jobId)
                .orderByDesc(BattleCard::getCreatedAt)
                .last("LIMIT 1"));
        if (card == null) {
            throw new NotFoundException("Battle card not found");
        }
        return BattleCardResponse.from(card);
    }

    public BattleCard requireMine(Long id) {
        BattleCard card = battleCardMapper.selectById(id);
        if (card == null) {
            throw new NotFoundException("Battle card not found");
        }
        userScope.requireOwner(SecurityUtils.currentUserId(), card.getUserId());
        return card;
    }

    private BattleCardAiResult getOrCreateAiResult(Long userId, String prompt, String requestHash) {
        String cacheKey = RedisKeys.battleCardCache(requestHash);
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached instanceof String cachedJson) {
            return parseAiResult(cachedJson);
        }

        AiResponse response = aiProvider.chat(new AiRequest("battle-card", prompt, aiProperties.getModel(), requestHash));
        BattleCardAiResult result = parseAiResult(response.getContent());
        redisTemplate.opsForValue().set(cacheKey, response.getContent(), Duration.ofHours(24));
        aiCallLogService.log(userId, "battle-card", requestHash, response);
        return result;
    }

    private BattleCardAiResult parseAiResult(String json) {
        try {
            return objectMapper.readValue(json, BattleCardAiResult.class);
        } catch (Exception exception) {
            throw new BusinessException(HttpStatus.BAD_GATEWAY, "AI battle-card response is not valid JSON");
        }
    }

    private BattleCard toEntity(BattleCardAiResult result, Long userId, Resume resume, JobPost jobPost, String requestHash) {
        BattleCard card = new BattleCard();
        card.setUserId(userId);
        card.setResumeId(resume.getId());
        card.setJobPostId(jobPost.getId());
        card.setMatchScore(result.getMatchScore());
        card.setCoreRequirements(result.getCoreRequirements());
        card.setSkillBreakdown(result.getSkillBreakdown());
        card.setMatchedPoints(result.getMatchedPoints());
        card.setWeakPoints(result.getWeakPoints());
        card.setResumeSuggestions(result.getResumeSuggestions());
        card.setInterviewFocus(result.getInterviewFocus());
        card.setThreeDayPlan(result.getThreeDayPlan());
        card.setSevenDayPlan(result.getSevenDayPlan());
        card.setRiskTips(result.getRiskTips());
        card.setRawAiResult(toJson(result));
        card.setRequestHash(requestHash);
        card.setPromptVersion(BattleCardPromptBuilder.PROMPT_VERSION);
        return card;
    }

    private String toJson(BattleCardAiResult result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception exception) {
            return "{}";
        }
    }
}
