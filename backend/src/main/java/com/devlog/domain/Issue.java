package com.devlog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(length = 2000)
    private String cause;

    @Column(length = 4000)
    private String solution;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IssueStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_log_id")
    private WorkLog workLog;

    public Issue(
            String title,
            String content,
            String cause,
            String solution,
            IssueStatus status,
            Project project,
            WorkLog workLog
    ) {
        this.title = title;
        this.content = content;
        this.cause = cause;
        this.solution = solution;
        this.status = status;
        this.project = project;
        this.workLog = workLog;
    }

    public void update(
            String title,
            String content,
            String cause,
            String solution,
            IssueStatus status,
            Project project,
            WorkLog workLog
    ) {
        this.title = title;
        this.content = content;
        this.cause = cause;
        this.solution = solution;
        this.status = status;
        this.project = project;
        this.workLog = workLog;
    }
}
