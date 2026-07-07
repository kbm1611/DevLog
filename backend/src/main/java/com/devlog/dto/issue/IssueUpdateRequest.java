package com.devlog.dto.issue;

import com.devlog.domain.IssueStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IssueUpdateRequest(
        @NotBlank
        @Size(max = 200)
        String title,

        @NotBlank
        @Size(max = 4000)
        String content,

        @Size(max = 2000)
        String cause,

        @Size(max = 4000)
        String solution,

        @NotNull
        IssueStatus status,

        @NotNull
        Long projectId,

        Long workLogId
) {
}
