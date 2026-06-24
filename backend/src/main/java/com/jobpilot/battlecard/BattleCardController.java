package com.jobpilot.battlecard;

import com.jobpilot.battlecard.dto.BattleCardGenerateRequest;
import com.jobpilot.battlecard.dto.BattleCardResponse;
import com.jobpilot.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BattleCardController {
    private final BattleCardService battleCardService;

    public BattleCardController(BattleCardService battleCardService) {
        this.battleCardService = battleCardService;
    }

    @PostMapping("/battle-cards/generate")
    public ApiResponse<BattleCardResponse> generate(@Valid @RequestBody BattleCardGenerateRequest request) {
        return ApiResponse.ok(battleCardService.generate(request));
    }

    @GetMapping("/battle-cards/{id}")
    public ApiResponse<BattleCardResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(battleCardService.getMine(id));
    }

    @GetMapping("/jobs/{jobId}/battle-card")
    public ApiResponse<BattleCardResponse> byJob(@PathVariable Long jobId) {
        return ApiResponse.ok(battleCardService.getByJob(jobId));
    }
}
