package com.jobpilot.ai;

import java.util.List;

public interface EmbeddingProvider {
    List<Float> embed(String text);
}
