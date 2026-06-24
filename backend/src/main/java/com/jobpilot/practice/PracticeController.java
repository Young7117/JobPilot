package com.jobpilot.practice;

import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.practice.dto.PracticeAnswerRequest;
import com.jobpilot.practice.dto.PracticeResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions/{id}")
public class PracticeController {
    private final PracticeService practiceService;

    public PracticeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }

    @PostMapping("/practice")
    public ApiResponse<PracticeResponse> submit(@PathVariable Long id, @Valid @RequestBody PracticeAnswerRequest request) {
        return ApiResponse.ok(practiceService.submit(id, request));
    }

    @PostMapping("/evaluate")
    public ApiResponse<PracticeResponse> evaluate(@PathVariable Long id, @Valid @RequestBody PracticeAnswerRequest request) {
        return ApiResponse.ok(practiceService.evaluate(id, request));
    }

    @GetMapping("/practices")
    public ApiResponse<List<PracticeResponse>> history(@PathVariable Long id) {
        return ApiResponse.ok(practiceService.history(id));
    }
}
