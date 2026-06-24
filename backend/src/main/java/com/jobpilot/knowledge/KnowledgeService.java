package com.jobpilot.knowledge;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jobpilot.common.error.NotFoundException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.common.security.UserScope;
import com.jobpilot.knowledge.dto.KnowledgeRequest;
import com.jobpilot.knowledge.dto.KnowledgeResponse;
import com.jobpilot.practice.PracticeService;
import com.jobpilot.practice.dto.PracticeResponse;
import com.jobpilot.question.QuestionWorkflowService;
import com.jobpilot.question.dto.QuestionResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class KnowledgeService {
    private final PersonalKnowledgeMapper mapper;
    private final UserScope userScope;
    private final QuestionWorkflowService questionWorkflowService;
    private final PracticeService practiceService;

    public KnowledgeService(
            PersonalKnowledgeMapper mapper,
            UserScope userScope,
            QuestionWorkflowService questionWorkflowService,
            PracticeService practiceService
    ) {
        this.mapper = mapper;
        this.userScope = userScope;
        this.questionWorkflowService = questionWorkflowService;
        this.practiceService = practiceService;
    }

    public KnowledgeResponse create(KnowledgeRequest request) {
        PersonalKnowledgeItem item = new PersonalKnowledgeItem();
        apply(item, request);
        item.setUserId(SecurityUtils.currentUserId());
        item.setSourceType("manual");
        mapper.insert(item);
        return KnowledgeResponse.from(item);
    }

    public List<KnowledgeResponse> list(String keyword, String category, String masteryLevel) {
        Long userId = SecurityUtils.currentUserId();
        LambdaQueryWrapper<PersonalKnowledgeItem> query = new LambdaQueryWrapper<PersonalKnowledgeItem>()
                .eq(PersonalKnowledgeItem::getUserId, userId)
                .orderByDesc(PersonalKnowledgeItem::getUpdatedAt);
        if (StringUtils.hasText(category)) {
            query.eq(PersonalKnowledgeItem::getCategory, category);
        }
        if (StringUtils.hasText(masteryLevel)) {
            query.eq(PersonalKnowledgeItem::getMasteryLevel, masteryLevel);
        }
        if (StringUtils.hasText(keyword)) {
            query.and(wrapper -> wrapper.like(PersonalKnowledgeItem::getTitle, keyword)
                    .or()
                    .like(PersonalKnowledgeItem::getContent, keyword));
        }
        return mapper.selectList(query).stream().map(KnowledgeResponse::from).toList();
    }

    public KnowledgeResponse get(Long id) {
        return KnowledgeResponse.from(requireMine(id));
    }

    public KnowledgeResponse update(Long id, KnowledgeRequest request) {
        PersonalKnowledgeItem item = requireMine(id);
        apply(item, request);
        mapper.updateById(item);
        return KnowledgeResponse.from(item);
    }

    public void delete(Long id) {
        mapper.deleteById(requireMine(id).getId());
    }

    public KnowledgeResponse saveQuestion(Long userQuestionId) {
        QuestionResponse question = questionWorkflowService.getMine(userQuestionId);
        List<PracticeResponse> history = practiceService.history(userQuestionId);
        PracticeResponse latest = history.isEmpty() ? null : history.get(0);
        PersonalKnowledgeItem item = new PersonalKnowledgeItem();
        item.setUserId(SecurityUtils.currentUserId());
        item.setTitle(question.getTitle());
        item.setCategory(question.getQuestionType());
        item.setTags(question.getTags());
        item.setContent(buildKnowledgeContent(question, latest));
        item.setSourceType("practice");
        item.setSourceId(latest == null ? null : latest.getId());
        item.setSourceQuestionId(userQuestionId);
        item.setMasteryLevel("needs_review");
        mapper.insert(item);
        return KnowledgeResponse.from(item);
    }

    private PersonalKnowledgeItem requireMine(Long id) {
        PersonalKnowledgeItem item = mapper.selectById(id);
        if (item == null) {
            throw new NotFoundException("Knowledge item not found");
        }
        userScope.requireOwner(SecurityUtils.currentUserId(), item.getUserId());
        return item;
    }

    private void apply(PersonalKnowledgeItem item, KnowledgeRequest request) {
        item.setTitle(request.getTitle());
        item.setContent(request.getContent());
        item.setCategory(request.getCategory());
        item.setTags(request.getTags());
        item.setMasteryLevel(StringUtils.hasText(request.getMasteryLevel()) ? request.getMasteryLevel() : "needs_review");
    }

    private String buildKnowledgeContent(QuestionResponse question, PracticeResponse latest) {
        if (latest == null) {
            return "题目：" + question.getContent() + "\n\n参考答案：\n" + question.getReferenceAnswer();
        }
        return """
                题目：
                %s

                我的回答：
                %s

                AI 优化回答：
                %s

                追问：
                %s
                """.formatted(
                question.getContent(),
                latest.getUserAnswer(),
                latest.getAiOptimizedAnswer(),
                latest.getAiFollowUp()
        );
    }
}
