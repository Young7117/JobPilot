package com.jobpilot.dashboard;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jobpilot.application.ApplicationRecord;
import com.jobpilot.application.ApplicationRecordMapper;
import com.jobpilot.battlecard.BattleCard;
import com.jobpilot.battlecard.BattleCardMapper;
import com.jobpilot.common.api.ApiResponse;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.job.JobPost;
import com.jobpilot.job.JobPostMapper;
import com.jobpilot.question.UserQuestion;
import com.jobpilot.question.UserQuestionMapper;
import com.jobpilot.resume.Resume;
import com.jobpilot.resume.ResumeMapper;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final ResumeMapper resumeMapper;
    private final JobPostMapper jobPostMapper;
    private final BattleCardMapper battleCardMapper;
    private final UserQuestionMapper userQuestionMapper;
    private final ApplicationRecordMapper applicationRecordMapper;

    public DashboardController(
            ResumeMapper resumeMapper,
            JobPostMapper jobPostMapper,
            BattleCardMapper battleCardMapper,
            UserQuestionMapper userQuestionMapper,
            ApplicationRecordMapper applicationRecordMapper
    ) {
        this.resumeMapper = resumeMapper;
        this.jobPostMapper = jobPostMapper;
        this.battleCardMapper = battleCardMapper;
        this.userQuestionMapper = userQuestionMapper;
        this.applicationRecordMapper = applicationRecordMapper;
    }

    @GetMapping
    public ApiResponse<DashboardResponse> summary() {
        Long userId = SecurityUtils.currentUserId();
        DashboardResponse response = new DashboardResponse();
        response.setResumeCount(resumeMapper.selectCount(new LambdaQueryWrapper<Resume>().eq(Resume::getUserId, userId)));
        response.setJobCount(jobPostMapper.selectCount(new LambdaQueryWrapper<JobPost>().eq(JobPost::getUserId, userId)));
        response.setBattleCardCount(battleCardMapper.selectCount(new LambdaQueryWrapper<BattleCard>().eq(BattleCard::getUserId, userId)));
        response.setQuestionCount(userQuestionMapper.selectCount(new LambdaQueryWrapper<UserQuestion>().eq(UserQuestion::getUserId, userId)));
        response.setReviewQueueCount(userQuestionMapper.selectCount(new LambdaQueryWrapper<UserQuestion>()
                .eq(UserQuestion::getUserId, userId)
                .eq(UserQuestion::getStatus, "needs_review")));
        Map<String, Long> stats = applicationRecordMapper.selectList(new LambdaQueryWrapper<ApplicationRecord>().eq(ApplicationRecord::getUserId, userId))
                .stream()
                .collect(Collectors.groupingBy(ApplicationRecord::getStatus, Collectors.counting()));
        response.setApplicationStatusStats(stats);
        return ApiResponse.ok(response);
    }
}
