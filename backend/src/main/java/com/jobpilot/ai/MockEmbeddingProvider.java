package com.jobpilot.ai;

import com.jobpilot.config.properties.EmbeddingProperties;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MockEmbeddingProvider implements EmbeddingProvider {
    private final EmbeddingProperties properties;

    public MockEmbeddingProvider(EmbeddingProperties properties) {
        this.properties = properties;
    }

    @Override
    public List<Float> embed(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        int dimension = Math.max(8, properties.getDimension());
        List<Float> vector = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            int value = bytes.length == 0 ? i : bytes[i % bytes.length];
            vector.add((value % 97) / 97.0f);
        }
        return vector;
    }
}
