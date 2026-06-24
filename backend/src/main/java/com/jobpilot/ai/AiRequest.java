package com.jobpilot.ai;

public class AiRequest {
    private final String scene;
    private final String prompt;
    private final String model;
    private final String requestHash;

    public AiRequest(String scene, String prompt, String model, String requestHash) {
        this.scene = scene;
        this.prompt = prompt;
        this.model = model;
        this.requestHash = requestHash;
    }

    public String getScene() {
        return scene;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getModel() {
        return model;
    }

    public String getRequestHash() {
        return requestHash;
    }
}
