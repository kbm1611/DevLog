package com.devlog.dto.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TodoUpdateRequest(
        @NotNull
        LocalDate todoDate,

        @NotBlank
        @Size(max = 200)
        String content,

        boolean completed,

        Long projectId
) {
}
