package com.jobpilot.question;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jobpilot.ai.EmbeddingProvider;
import com.jobpilot.config.properties.QdrantProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class QuestionIndexService {
    private final QuestionBankMapper questionBankMapper;
    private final EmbeddingProvider embeddingProvider;
    private final QdrantProperties qdrantProperties;
    private final RestClient restClient;

    public QuestionIndexService(
            QuestionBankMapper questionBankMapper,
            EmbeddingProvider embeddingProvider,
            QdrantProperties qdrantProperties,
            RestClient.Builder restClientBuilder
    ) {
        this.questionBankMapper = questionBankMapper;
        this.embeddingProvider = embeddingProvider;
        this.qdrantProperties = qdrantProperties;
        this.restClient = restClientBuilder.build();
    }

    public void indexAllPublicQuestions() {
        List<QuestionBank> questions = questionBankMapper.selectList(new LambdaQueryWrapper<QuestionBank>());
        List<Map<String, Object>> points = questions.stream().map(this::toPoint).toList();
        Map<String, Object> body = Map.of("points", points);
        restClient.put()
                .uri(qdrantProperties.getHost() + "/collections/" + qdrantProperties.getCollection() + "/points")
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private Map<String, Object> toPoint(QuestionBank question) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("questionId", question.getId());
        payload.put("title", question.getTitle());
        payload.put("questionType", question.getQuestionType());
        payload.put("difficulty", question.getDifficulty());
        payload.put("tags", question.getTags());
        payload.put("qualityScore", question.getQualityScore());
        return Map.of(
                "id", question.getId(),
                "vector", embeddingProvider.embed(question.getTitle() + "\n" + question.getContent()),
                "payload", payload
        );
    }
}
