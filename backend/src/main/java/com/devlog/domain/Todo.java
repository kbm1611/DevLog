package com.devlog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate todoDate;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Todo(LocalDate todoDate, String content, boolean completed, Project project) {
        this.todoDate = todoDate;
        this.content = content;
        this.completed = completed;
        this.project = project;
    }

    public void update(LocalDate todoDate, String content, boolean completed, Project project) {
        this.todoDate = todoDate;
        this.content = content;
        this.completed = completed;
        this.project = project;
    }
}
