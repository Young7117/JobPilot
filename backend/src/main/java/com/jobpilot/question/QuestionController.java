package com.jobpilot.question;

import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.question.dto.GenerateQuestionsRequest;
import com.jobpilot.question.dto.QuestionResponse;
import com.jobpilot.question.dto.UpdateQuestionStatusRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionWorkflowService questionWorkflowService;

    public QuestionController(QuestionWorkflowService questionWorkflowService) {
        this.questionWorkflowService = questionWorkflowService;
    }

    @PostMapping("/generate-for-job")
    public ApiResponse<List<QuestionResponse>> generateForJob(@Valid @RequestBody GenerateQuestionsRequest request) {
        return ApiResponse.ok(questionWorkflowService.generateForJob(request));
    }

    @GetMapping
    public ApiResponse<List<QuestionResponse>> list() {
        return ApiResponse.ok(questionWorkflowService.listMine());
    }

    @GetMapping("/{id}")
    public ApiResponse<QuestionResponse> detail(@org.springframework.web.bind.annotation.PathVariable Long id) {
        return ApiResponse.ok(questionWorkflowService.getMine(id));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<QuestionResponse> updateStatus(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @Valid @RequestBody UpdateQuestionStatusRequest request
    ) {
        return ApiResponse.ok(questionWorkflowService.updateStatus(id, request.getStatus()));
    }
}
