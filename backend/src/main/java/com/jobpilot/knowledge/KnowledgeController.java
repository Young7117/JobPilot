package com.jobpilot.knowledge;

import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.knowledge.dto.KnowledgeRequest;
import com.jobpilot.knowledge.dto.KnowledgeResponse;
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
@RequestMapping("/api")
public class KnowledgeController {
    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @PostMapping("/knowledge")
    public ApiResponse<KnowledgeResponse> create(@Valid @RequestBody KnowledgeRequest request) {
        return ApiResponse.ok(knowledgeService.create(request));
    }

    @GetMapping("/knowledge")
    public ApiResponse<List<KnowledgeResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String masteryLevel
    ) {
        return ApiResponse.ok(knowledgeService.list(keyword, category, masteryLevel));
    }

    @GetMapping("/knowledge/{id}")
    public ApiResponse<KnowledgeResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(knowledgeService.get(id));
    }

    @PutMapping("/knowledge/{id}")
    public ApiResponse<KnowledgeResponse> update(@PathVariable Long id, @Valid @RequestBody KnowledgeRequest request) {
        return ApiResponse.ok(knowledgeService.update(id, request));
    }

    @DeleteMapping("/knowledge/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        knowledgeService.delete(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/questions/{id}/save-to-knowledge")
    public ApiResponse<KnowledgeResponse> saveQuestion(@PathVariable Long id) {
        return ApiResponse.ok(knowledgeService.saveQuestion(id));
    }
}
