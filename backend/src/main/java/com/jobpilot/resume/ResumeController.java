package com.jobpilot.resume;

import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.resume.dto.ResumeCreateRequest;
import com.jobpilot.resume.dto.ResumeResponse;
import com.jobpilot.resume.dto.ResumeUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/text")
    public ApiResponse<ResumeResponse> createText(@Valid @RequestBody ResumeCreateRequest request) {
        return ApiResponse.ok(resumeService.createText(request));
    }

    @PostMapping("/upload")
    public ApiResponse<ResumeResponse> upload(
            @RequestParam @NotBlank String title,
            @RequestParam(required = false) String targetRole,
            @RequestParam MultipartFile file
    ) {
        return ApiResponse.ok(resumeService.upload(title, targetRole, file));
    }

    @GetMapping
    public ApiResponse<List<ResumeResponse>> list() {
        return ApiResponse.ok(resumeService.listMine());
    }

    @GetMapping("/{id}")
    public ApiResponse<ResumeResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(resumeService.getMine(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ResumeResponse> update(@PathVariable Long id, @Valid @RequestBody ResumeUpdateRequest request) {
        return ApiResponse.ok(resumeService.update(id, request));
    }

    @PutMapping("/{id}/default")
    public ApiResponse<ResumeResponse> setDefault(@PathVariable Long id) {
        return ApiResponse.ok(resumeService.setDefault(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        resumeService.delete(id);
        return ApiResponse.ok(null);
    }
}
