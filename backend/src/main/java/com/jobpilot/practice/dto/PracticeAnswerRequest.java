package com.jobpilot.practice.dto;

import jakarta.validation.constraints.NotBlank;

public class PracticeAnswerRequest {
    @NotBlank
    private String userAnswer;

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
}
