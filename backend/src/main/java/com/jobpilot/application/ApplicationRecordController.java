package com.jobpilot.application;

import com.jobpilot.application.dto.ApplicationRecordRequest;
import com.jobpilot.application.dto.ApplicationRecordResponse;
import com.jobpilot.common.api.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
public class ApplicationRecordController {
    private final ApplicationRecordService service;

    public ApplicationRecordController(ApplicationRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<ApplicationRecordResponse> create(@Valid @RequestBody ApplicationRecordRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @GetMapping
    public ApiResponse<List<ApplicationRecordResponse>> list() {
        return ApiResponse.ok(service.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<ApplicationRecordResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ApplicationRecordResponse> update(@PathVariable Long id, @Valid @RequestBody ApplicationRecordRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @PostMapping("/{id}/review")
    public ApiResponse<ApplicationRecordResponse> review(@PathVariable Long id) {
        return ApiResponse.ok(service.review(id));
    }
}
