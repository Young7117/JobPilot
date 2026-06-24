package com.jobpilot.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobpilot.ai.AiCallLogService;
import com.jobpilot.ai.AiProvider;
import com.jobpilot.ai.AiRequest;
import com.jobpilot.ai.AiResponse;
import com.jobpilot.application.dto.ApplicationRecordRequest;
import com.jobpilot.application.dto.ApplicationRecordResponse;
import com.jobpilot.application.dto.ApplicationReviewResult;
import com.jobpilot.battlecard.BattleCardService;
import com.jobpilot.common.error.BusinessException;
import com.jobpilot.common.error.NotFoundException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.common.security.UserScope;
import com.jobpilot.config.properties.AiProperties;
import com.jobpilot.job.JobPostService;
import com.jobpilot.resume.ResumeService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ApplicationRecordService {
    private final ApplicationRecordMapper mapper;
    private final JobPostService jobPostService;
    private final ResumeService resumeService;
    private final BattleCardService battleCardService;
    private final UserScope userScope;
    private final AiProvider aiProvider;
    private final AiProperties aiProperties;
    private final AiCallLogService aiCallLogService;
    private final ObjectMapper objectMapper;

    public ApplicationRecordService(
            ApplicationRecordMapper mapper,
            JobPostService jobPostService,
            ResumeService resumeService,
            BattleCardService battleCardService,
            UserScope userScope,
            AiProvider aiProvider,
            AiProperties aiProperties,
            AiCallLogService aiCallLogService,
            ObjectMapper objectMapper
    ) {
        this.mapper = mapper;
        this.jobPostService = jobPostService;
        this.resumeService = resumeService;
        this.battleCardService = battleCardService;
        this.userScope = userScope;
        this.aiProvider = aiProvider;
        this.aiProperties = aiProperties;
        this.aiCallLogService = aiCallLogService;
        this.objectMapper = objectMapper;
    }

    public ApplicationRecordResponse create(ApplicationRecordRequest request) {
        validateLinks(request);
        ApplicationRecord record = new ApplicationRecord();
        apply(record, request);
        record.setUserId(SecurityUtils.currentUserId());
        mapper.insert(record);
        return ApplicationRecordResponse.from(record);
    }

    public List<ApplicationRecordResponse> list() {
        Long userId = SecurityUtils.currentUserId();
        return mapper.selectList(new LambdaQueryWrapper<ApplicationRecord>()
                        .eq(ApplicationRecord::getUserId, userId)
                        .orderByDesc(ApplicationRecord::getUpdatedAt))
                .stream()
                .map(ApplicationRecordResponse::from)
                .toList();
    }

    public ApplicationRecordResponse get(Long id) {
        return ApplicationRecordResponse.from(requireMine(id));
    }

    public ApplicationRecordResponse update(Long id, ApplicationRecordRequest request) {
        validateLinks(request);
        ApplicationRecord record = requireMine(id);
        apply(record, request);
        mapper.updateById(record);
        return ApplicationRecordResponse.from(record);
    }

    public ApplicationRecordResponse review(Long id) {
        Long userId = SecurityUtils.currentUserId();
        ApplicationRecord record = requireMine(id);
        AiResponse response = aiProvider.chat(new AiRequest("application-review", buildPrompt(record), aiProperties.getModel(), null));
        aiCallLogService.log(userId, "application-review", null, response);
        ApplicationReviewResult result = parse(response.getContent());
        record.setAiReviewSuggestions(result.getSuggestions());
        mapper.updateById(record);
        return ApplicationRecordResponse.from(record);
    }

    public ApplicationRecord requireMine(Long id) {
        ApplicationRecord record = mapper.selectById(id);
        if (record == null) {
            throw new NotFoundException("Application record not found");
        }
        userScope.requireOwner(SecurityUtils.currentUserId(), record.getUserId());
        return record;
    }

    private void validateLinks(ApplicationRecordRequest request) {
        jobPostService.requireMine(request.getJobPostId());
        if (request.getResumeId() != null) {
            resumeService.requireOwnedEntity(request.getResumeId());
        }
        if (request.getBattleCardId() != null) {
            battleCardService.requireMine(request.getBattleCardId());
        }
    }

    private void apply(ApplicationRecord record, ApplicationRecordRequest request) {
        record.setJobPostId(request.getJobPostId());
        record.setResumeId(request.getResumeId());
        record.setBattleCardId(request.getBattleCardId());
        record.setStatus(StringUtils.hasText(request.getStatus()) ? request.getStatus() : "planned");
        record.setApplyDate(request.getApplyDate());
        record.setInterviewDate(request.getInterviewDate());
        record.setResult(request.getResult());
        record.setFailureReason(request.getFailureReason());
        record.setNote(request.getNote());
    }

    private String buildPrompt(ApplicationRecord record) {
        return "请基于投递状态、结果和失败原因生成复盘建议：" + record.getStatus() + " " + record.getResult() + " " + record.getFailureReason();
    }

    private ApplicationReviewResult parse(String json) {
        try {
            return objectMapper.readValue(json, ApplicationReviewResult.class);
        } catch (Exception exception) {
            throw new BusinessException(HttpStatus.BAD_GATEWAY, "AI application review response is not valid JSON");
        }
    }
}
