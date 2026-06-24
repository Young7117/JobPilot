package com.jobpilot.application.dto;

import java.util.List;

public class ApplicationReviewResult {
    private List<String> suggestions;

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
}
