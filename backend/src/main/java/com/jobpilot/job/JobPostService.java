package com.jobpilot.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jobpilot.common.error.NotFoundException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.common.security.UserScope;
import com.jobpilot.job.dto.JobPostRequest;
import com.jobpilot.job.dto.JobPostResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JobPostService {
    private final JobPostMapper jobPostMapper;
    private final UserScope userScope;

    public JobPostService(JobPostMapper jobPostMapper, UserScope userScope) {
        this.jobPostMapper = jobPostMapper;
        this.userScope = userScope;
    }

    public JobPostResponse create(JobPostRequest request) {
        JobPost jobPost = new JobPost();
        applyRequest(jobPost, request);
        jobPost.setUserId(SecurityUtils.currentUserId());
        jobPostMapper.insert(jobPost);
        return JobPostResponse.from(jobPost);
    }

    public List<JobPostResponse> listMine(String jobDirection) {
        Long userId = SecurityUtils.currentUserId();
        LambdaQueryWrapper<JobPost> query = new LambdaQueryWrapper<JobPost>()
                .eq(JobPost::getUserId, userId)
                .orderByDesc(JobPost::getUpdatedAt);
        if (StringUtils.hasText(jobDirection)) {
            query.eq(JobPost::getJobDirection, jobDirection);
        }
        return jobPostMapper.selectList(query).stream().map(JobPostResponse::from).toList();
    }

    public JobPostResponse getMine(Long id) {
        return JobPostResponse.from(requireMine(id));
    }

    public JobPostResponse update(Long id, JobPostRequest request) {
        JobPost jobPost = requireMine(id);
        applyRequest(jobPost, request);
        jobPostMapper.updateById(jobPost);
        return JobPostResponse.from(jobPost);
    }

    public void delete(Long id) {
        JobPost jobPost = requireMine(id);
        jobPostMapper.deleteById(jobPost.getId());
    }

    public JobPost requireMine(Long id) {
        JobPost jobPost = jobPostMapper.selectById(id);
        if (jobPost == null) {
            throw new NotFoundException("Job post not found");
        }
        userScope.requireOwner(SecurityUtils.currentUserId(), jobPost.getUserId());
        return jobPost;
    }

    private void applyRequest(JobPost jobPost, JobPostRequest request) {
        jobPost.setCompanyName(request.getCompanyName());
        jobPost.setPositionName(request.getPositionName());
        jobPost.setIndustry(StringUtils.hasText(request.getIndustry()) ? request.getIndustry() : "计算机/互联网");
        jobPost.setCity(request.getCity());
        jobPost.setSalaryRange(request.getSalaryRange());
        jobPost.setJobDirection(request.getJobDirection());
        jobPost.setJdContent(request.getJdContent());
        jobPost.setSource(request.getSource());
        jobPost.setStatus(StringUtils.hasText(request.getStatus()) ? request.getStatus() : "preparing");
    }
}
