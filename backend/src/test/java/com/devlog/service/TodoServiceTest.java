package com.devlog.service;

import com.devlog.dto.project.ProjectCreateRequest;
import com.devlog.dto.project.ProjectResponse;
import com.devlog.dto.todo.TodoCreateRequest;
import com.devlog.dto.todo.TodoResponse;
import com.devlog.dto.todo.TodoUpdateRequest;
import com.devlog.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class TodoServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TodoService todoService;

    @Test
    @DisplayName("creates and finds todos by date")
    void createAndFindTodosByDate() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("Public Maintenance", "Operation"));
        LocalDate todoDate = LocalDate.of(2026, 7, 8);

        todoService.create(new TodoCreateRequest(todoDate, "Write incident report", false, project.id()));
        todoService.create(new TodoCreateRequest(todoDate.plusDays(1), "Other date todo", false, project.id()));

        List<TodoResponse> results = todoService.findAll(todoDate);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).content()).isEqualTo("Write incident report");
        assertThat(results.get(0).projectName()).isEqualTo("Public Maintenance");
    }

    @Test
    @DisplayName("updates a todo and completion flag")
    void updateTodo() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("Admin System", "Development"));
        TodoResponse created = todoService.create(new TodoCreateRequest(
                LocalDate.of(2026, 7, 8),
                "Clean API spec",
                false,
                project.id()
        ));

        TodoResponse updated = todoService.update(created.id(), new TodoUpdateRequest(
                LocalDate.of(2026, 7, 8),
                "Clean API spec done",
                true,
                project.id()
        ));

        assertThat(updated.content()).isEqualTo("Clean API spec done");
        assertThat(updated.completed()).isTrue();
    }

    @Test
    @DisplayName("deletes a todo")
    void deleteTodo() {
        TodoResponse created = todoService.create(new TodoCreateRequest(
                LocalDate.of(2026, 7, 8),
                "Todo to delete",
                false,
                null
        ));

        todoService.delete(created.id());

        assertThatThrownBy(() -> todoService.findById(created.id()))
                .isInstanceOf(NotFoundException.class);
    }
}
