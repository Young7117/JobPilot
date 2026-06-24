package com.jobpilot.question.dto;

import java.util.List;

public class AiQuestionResult {
    private List<GeneratedQuestion> questions;

    public List<GeneratedQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<GeneratedQuestion> questions) {
        this.questions = questions;
    }

    public static class GeneratedQuestion {
        private String type;
        private String difficulty;
        private String title;
        private String content;
        private String referenceAnswer;
        private List<String> tags;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getReferenceAnswer() { return referenceAnswer; }
        public void setReferenceAnswer(String referenceAnswer) { this.referenceAnswer = referenceAnswer; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }
}
