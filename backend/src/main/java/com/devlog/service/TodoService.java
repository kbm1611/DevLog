package com.devlog.service;

import com.devlog.domain.Project;
import com.devlog.domain.Todo;
import com.devlog.dto.todo.TodoCreateRequest;
import com.devlog.dto.todo.TodoResponse;
import com.devlog.dto.todo.TodoUpdateRequest;
import com.devlog.exception.NotFoundException;
import com.devlog.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final ProjectService projectService;

    @Transactional
    public TodoResponse create(TodoCreateRequest request) {
        Project project = getOptionalProject(request.projectId());
        Todo todo = new Todo(
                request.todoDate(),
                request.content(),
                request.completed(),
                project
        );
        return TodoResponse.from(todoRepository.save(todo));
    }

    public List<TodoResponse> findAll(LocalDate todoDate) {
        return findByFilter(todoDate).stream()
                .map(TodoResponse::from)
                .toList();
    }

    public TodoResponse findById(Long id) {
        return TodoResponse.from(getTodo(id));
    }

    @Transactional
    public TodoResponse update(Long id, TodoUpdateRequest request) {
        Todo todo = getTodo(id);
        Project project = getOptionalProject(request.projectId());
        todo.update(
                request.todoDate(),
                request.content(),
                request.completed(),
                project
        );
        return TodoResponse.from(todo);
    }

    @Transactional
    public void delete(Long id) {
        Todo todo = getTodo(id);
        todoRepository.delete(todo);
    }

    public Todo getTodo(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found. id=" + id));
    }

    private List<Todo> findByFilter(LocalDate todoDate) {
        if (todoDate != null) {
            return todoRepository.findByTodoDateOrderByIdDesc(todoDate);
        }
        return todoRepository.findAllByOrderByTodoDateDescIdDesc();
    }

    private Project getOptionalProject(Long projectId) {
        if (projectId == null) {
            return null;
        }
        return projectService.getProject(projectId);
    }
}
