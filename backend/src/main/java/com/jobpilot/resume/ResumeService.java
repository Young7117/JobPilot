package com.jobpilot.resume;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jobpilot.common.error.NotFoundException;
import com.jobpilot.common.security.SecurityUtils;
import com.jobpilot.common.security.UserScope;
import com.jobpilot.resume.ResumeParser.ParsedResume;
import com.jobpilot.resume.dto.ResumeCreateRequest;
import com.jobpilot.resume.dto.ResumeResponse;
import com.jobpilot.resume.dto.ResumeUpdateRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeService {
    private final ResumeMapper resumeMapper;
    private final ResumeParser resumeParser;
    private final UserScope userScope;

    public ResumeService(ResumeMapper resumeMapper, ResumeParser resumeParser, UserScope userScope) {
        this.resumeMapper = resumeMapper;
        this.resumeParser = resumeParser;
        this.userScope = userScope;
    }

    public ResumeResponse createText(ResumeCreateRequest request) {
        Resume resume = new Resume();
        resume.setUserId(SecurityUtils.currentUserId());
        resume.setTitle(request.getTitle());
        resume.setTargetRole(request.getTargetRole());
        resume.setContent(request.getContent());
        resume.setFileType("text");
        resume.setVersion(nextVersion(resume.getUserId()));
        resume.setIsDefault(false);
        resumeMapper.insert(resume);
        return ResumeResponse.from(resume);
    }

    public ResumeResponse upload(String title, String targetRole, MultipartFile file) {
        ParsedResume parsed = resumeParser.parse(file);
        Resume resume = new Resume();
        resume.setUserId(SecurityUtils.currentUserId());
        resume.setTitle(title);
        resume.setTargetRole(targetRole);
        resume.setContent(parsed.getContent());
        resume.setFileType(parsed.getFileType());
        resume.setVersion(nextVersion(resume.getUserId()));
        resume.setIsDefault(false);
        resumeMapper.insert(resume);
        return ResumeResponse.from(resume);
    }

    public List<ResumeResponse> listMine() {
        Long userId = SecurityUtils.currentUserId();
        return resumeMapper.selectList(new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getUserId, userId)
                        .orderByDesc(Resume::getIsDefault)
                        .orderByDesc(Resume::getUpdatedAt))
                .stream()
                .map(ResumeResponse::from)
                .toList();
    }

    public ResumeResponse getMine(Long id) {
        return ResumeResponse.from(requireMine(id));
    }

    public Resume requireOwnedEntity(Long id) {
        return requireMine(id);
    }

    public ResumeResponse update(Long id, ResumeUpdateRequest request) {
        Resume resume = requireMine(id);
        resume.setTitle(request.getTitle());
        resume.setTargetRole(request.getTargetRole());
        resume.setContent(request.getContent());
        resumeMapper.updateById(resume);
        return ResumeResponse.from(resume);
    }

    public void delete(Long id) {
        Resume resume = requireMine(id);
        resumeMapper.deleteById(resume.getId());
    }

    @Transactional
    public ResumeResponse setDefault(Long id) {
        Resume resume = requireMine(id);
        resumeMapper.update(null, new LambdaUpdateWrapper<Resume>()
                .eq(Resume::getUserId, resume.getUserId())
                .set(Resume::getIsDefault, false));
        resume.setIsDefault(true);
        resumeMapper.updateById(resume);
        return ResumeResponse.from(resume);
    }

    private Resume requireMine(Long id) {
        Resume resume = resumeMapper.selectById(id);
        if (resume == null) {
            throw new NotFoundException("Resume not found");
        }
        userScope.requireOwner(SecurityUtils.currentUserId(), resume.getUserId());
        return resume;
    }

    private int nextVersion(Long userId) {
        Long count = resumeMapper.selectCount(new LambdaQueryWrapper<Resume>().eq(Resume::getUserId, userId));
        return count == null ? 1 : count.intValue() + 1;
    }
}
