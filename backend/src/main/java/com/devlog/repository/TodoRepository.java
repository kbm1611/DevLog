package com.devlog.repository;

import com.devlog.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByTodoDateOrderByIdDesc(LocalDate todoDate);

    List<Todo> findByTodoDateBetweenOrderByTodoDateAscIdAsc(LocalDate startDate, LocalDate endDate);

    List<Todo> findAllByOrderByTodoDateDescIdDesc();
}
