package com.devlog.dto.issue;

import com.devlog.domain.Issue;
import com.devlog.domain.IssueStatus;

import java.time.LocalDateTime;

public record IssueResponse(
        Long id,
        String title,
        String content,
        String cause,
        String solution,
        IssueStatus status,
        Long projectId,
        String projectName,
        Long workLogId,
        String workLogTitle,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static IssueResponse from(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getContent(),
                issue.getCause(),
                issue.getSolution(),
                issue.getStatus(),
                issue.getProject().getId(),
                issue.getProject().getName(),
                issue.getWorkLog() == null ? null : issue.getWorkLog().getId(),
                issue.getWorkLog() == null ? null : issue.getWorkLog().getTitle(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }
}
