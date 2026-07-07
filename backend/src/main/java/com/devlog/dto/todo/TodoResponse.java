package com.devlog.dto.todo;

import com.devlog.domain.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        LocalDate todoDate,
        String content,
        boolean completed,
        Long projectId,
        String projectName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTodoDate(),
                todo.getContent(),
                todo.isCompleted(),
                todo.getProject() == null ? null : todo.getProject().getId(),
                todo.getProject() == null ? null : todo.getProject().getName(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}
