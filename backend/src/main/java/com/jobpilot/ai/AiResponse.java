package com.jobpilot.ai;

public class AiResponse {
    private final String content;
    private final Integer promptTokens;
    private final Integer completionTokens;
    private final String modelName;
    private final Double cost;

    public AiResponse(String content, Integer promptTokens, Integer completionTokens, String modelName, Double cost) {
        this.content = content;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.modelName = modelName;
        this.cost = cost;
    }

    public String getContent() {
        return content;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public String getModelName() {
        return modelName;
    }

    public Double getCost() {
        return cost;
    }
}
