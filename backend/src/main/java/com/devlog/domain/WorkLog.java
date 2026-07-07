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
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate workDate;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkLogStatus status;

    @Column(length = 2000)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "workLog")
    private List<Issue> issues = new ArrayList<>();

    public WorkLog(LocalDate workDate, String title, WorkLogStatus status, String memo, Project project) {
        this.workDate = workDate;
        this.title = title;
        this.status = status;
        this.memo = memo;
        this.project = project;
    }

    public void update(LocalDate workDate, String title, WorkLogStatus status, String memo, Project project) {
        this.workDate = workDate;
        this.title = title;
        this.status = status;
        this.memo = memo;
        this.project = project;
    }
}
