package com.jobpilot.question;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class DuplicateQuestionService {
    private final QuestionBankMapper questionBankMapper;
    private final CandidateQuestionMapper candidateQuestionMapper;

    public DuplicateQuestionService(QuestionBankMapper questionBankMapper, CandidateQuestionMapper candidateQuestionMapper) {
        this.questionBankMapper = questionBankMapper;
        this.candidateQuestionMapper = candidateQuestionMapper;
    }

    public BigDecimal maxDuplicateScore(String title, String content) {
        String target = title + " " + content;
        double publicMax = questionBankMapper.selectList(new LambdaQueryWrapper<QuestionBank>())
                .stream()
                .mapToDouble(question -> similarity(target, question.getTitle() + " " + question.getContent()))
                .max()
                .orElse(0);
        double candidateMax = candidateQuestionMapper.selectList(new LambdaQueryWrapper<CandidateQuestion>())
                .stream()
                .mapToDouble(question -> similarity(target, question.getTitle() + " " + question.getContent()))
                .max()
                .orElse(0);
        return BigDecimal.valueOf(Math.max(publicMax, candidateMax) * 100)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private double similarity(String left, String right) {
        Set<String> leftTokens = tokens(left);
        Set<String> rightTokens = tokens(right);
        if (leftTokens.isEmpty() || rightTokens.isEmpty()) {
            return 0;
        }
        Set<String> intersection = new HashSet<>(leftTokens);
        intersection.retainAll(rightTokens);
        Set<String> union = new HashSet<>(leftTokens);
        union.addAll(rightTokens);
        return intersection.size() * 1.0 / union.size();
    }

    private Set<String> tokens(String value) {
        return new HashSet<>(Arrays.asList(value.toLowerCase().split("[\\s,，。；;：:、]+")));
    }
}
