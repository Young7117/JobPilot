package com.jobpilot.job;

import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.job.dto.JobPostRequest;
import com.jobpilot.job.dto.JobPostResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobPostController {
    private final JobPostService jobPostService;

    public JobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @PostMapping
    public ApiResponse<JobPostResponse> create(@Valid @RequestBody JobPostRequest request) {
        return ApiResponse.ok(jobPostService.create(request));
    }

    @GetMapping
    public ApiResponse<List<JobPostResponse>> list(@RequestParam(required = false) String jobDirection) {
        return ApiResponse.ok(jobPostService.listMine(jobDirection));
    }

    @GetMapping("/{id}")
    public ApiResponse<JobPostResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(jobPostService.getMine(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<JobPostResponse> update(@PathVariable Long id, @Valid @RequestBody JobPostRequest request) {
        return ApiResponse.ok(jobPostService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        jobPostService.delete(id);
        return ApiResponse.ok(null);
    }
}
