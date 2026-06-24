package com.jobpilot.question.dto;

import com.jobpilot.question.QuestionBank;
import com.jobpilot.question.CandidateQuestion;
import com.jobpilot.question.UserQuestion;
import java.util.List;

public class QuestionResponse {
    private Long userQuestionId;
    private Long questionId;
    private String title;
    private String content;
    private String questionType;
    private String difficulty;
    private List<String> tags;
    private String referenceAnswer;
    private String status;
    private String sourceType;

    public static QuestionResponse fromPublic(UserQuestion userQuestion, QuestionBank question) {
        QuestionResponse response = new QuestionResponse();
        response.userQuestionId = userQuestion.getId();
        response.questionId = question.getId();
        response.title = question.getTitle();
        response.content = question.getContent();
        response.questionType = question.getQuestionType();
        response.difficulty = question.getDifficulty();
        response.tags = question.getTags();
        response.referenceAnswer = question.getReferenceAnswer();
        response.status = userQuestion.getStatus();
        response.sourceType = userQuestion.getSourceType();
        return response;
    }

    public static QuestionResponse fromCandidate(UserQuestion userQuestion, CandidateQuestion question) {
        QuestionResponse response = new QuestionResponse();
        response.userQuestionId = userQuestion.getId();
        response.questionId = question.getId();
        response.title = question.getTitle();
        response.content = question.getContent();
        response.questionType = question.getQuestionType();
        response.difficulty = question.getDifficulty();
        response.tags = question.getTags();
        response.referenceAnswer = question.getReferenceAnswer();
        response.status = userQuestion.getStatus();
        response.sourceType = userQuestion.getSourceType();
        return response;
    }

    public Long getUserQuestionId() { return userQuestionId; }
    public Long getQuestionId() { return questionId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getQuestionType() { return questionType; }
    public String getDifficulty() { return difficulty; }
    public List<String> getTags() { return tags; }
    public String getReferenceAnswer() { return referenceAnswer; }
    public String getStatus() { return status; }
    public String getSourceType() { return sourceType; }
}
