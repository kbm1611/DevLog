package com.devlog.dto.worklog;

import com.devlog.domain.WorkLogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record WorkLogCreateRequest(
        @NotNull
        LocalDate workDate,

        @NotBlank
        @Size(max = 200)
        String title,

        @NotNull
        WorkLogStatus status,

        @Size(max = 2000)
        String memo,

        @NotNull
        Long projectId
) {
}
