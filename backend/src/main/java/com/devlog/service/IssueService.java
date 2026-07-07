package com.devlog.service;

import com.devlog.domain.Issue;
import com.devlog.domain.IssueStatus;
import com.devlog.domain.Project;
import com.devlog.domain.WorkLog;
import com.devlog.dto.issue.IssueCreateRequest;
import com.devlog.dto.issue.IssueResponse;
import com.devlog.dto.issue.IssueUpdateRequest;
import com.devlog.exception.NotFoundException;
import com.devlog.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectService projectService;
    private final WorkLogService workLogService;

    @Transactional
    public IssueResponse create(IssueCreateRequest request) {
        Project project = projectService.getProject(request.projectId());
        WorkLog workLog = getOptionalWorkLog(request.workLogId());
        Issue issue = new Issue(
                request.title(),
                request.content(),
                request.cause(),
                request.solution(),
                request.status(),
                project,
                workLog
        );
        return IssueResponse.from(issueRepository.save(issue));
    }

    public List<IssueResponse> findAll(IssueStatus status, String keyword) {
        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return issueRepository.search(status, normalizedKeyword).stream()
                .map(IssueResponse::from)
                .toList();
    }

    public IssueResponse findById(Long id) {
        return IssueResponse.from(getIssue(id));
    }

    @Transactional
    public IssueResponse update(Long id, IssueUpdateRequest request) {
        Issue issue = getIssue(id);
        Project project = projectService.getProject(request.projectId());
        WorkLog workLog = getOptionalWorkLog(request.workLogId());
        issue.update(
                request.title(),
                request.content(),
                request.cause(),
                request.solution(),
                request.status(),
                project,
                workLog
        );
        return IssueResponse.from(issue);
    }

    @Transactional
    public void delete(Long id) {
        Issue issue = getIssue(id);
        issueRepository.delete(issue);
    }

    public Issue getIssue(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Issue not found. id=" + id));
    }

    private WorkLog getOptionalWorkLog(Long workLogId) {
        if (workLogId == null) {
            return null;
        }
        return workLogService.getWorkLog(workLogId);
    }
}
