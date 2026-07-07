package com.devlog.dto.worklog;

import com.devlog.domain.WorkLog;
import com.devlog.domain.WorkLogStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record WorkLogResponse(
        Long id,
        LocalDate workDate,
        String title,
        WorkLogStatus status,
        String memo,
        Long projectId,
        String projectName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static WorkLogResponse from(WorkLog workLog) {
        return new WorkLogResponse(
                workLog.getId(),
                workLog.getWorkDate(),
                workLog.getTitle(),
                workLog.getStatus(),
                workLog.getMemo(),
                workLog.getProject().getId(),
                workLog.getProject().getName(),
                workLog.getCreatedAt(),
                workLog.getUpdatedAt()
        );
    }
}
