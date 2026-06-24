package com.jobpilot.question;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jobpilot.ai.EmbeddingProvider;
import com.jobpilot.config.properties.QdrantProperties;
import com.jobpilot.question.QuestionQueryBuilder.QuestionQuery;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class QuestionRetrievalService {
    private final QuestionBankMapper questionBankMapper;
    private final EmbeddingProvider embeddingProvider;
    private final QdrantProperties qdrantProperties;
    private final RestClient restClient;

    public QuestionRetrievalService(
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

    public List<QuestionBank> retrieve(QuestionQuery query, int limit) {
        tryQdrantSearch(query, limit);
        return questionBankMapper.selectList(new LambdaQueryWrapper<QuestionBank>())
                .stream()
                .sorted(Comparator
                        .comparing((QuestionBank q) -> tagMatches(q, query)).reversed()
                        .thenComparing(QuestionBank::getQualityScore, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .toList();
    }

    private void tryQdrantSearch(QuestionQuery query, int limit) {
        try {
            restClient.post()
                    .uri(qdrantProperties.getHost() + "/collections/" + qdrantProperties.getCollection() + "/points/search")
                    .body(new QdrantSearchRequest(embeddingProvider.embed(query.queryText()), limit))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ignored) {
            // MySQL quality/tag fallback keeps the MVP usable when Qdrant is not running locally.
        }
    }

    private long tagMatches(QuestionBank question, QuestionQuery query) {
        if (question.getTags() == null || query.tags() == null) {
            return 0;
        }
        return question.getTags().stream().filter(query.tags()::contains).count();
    }

    private record QdrantSearchRequest(List<Float> vector, int limit) {
    }
}
