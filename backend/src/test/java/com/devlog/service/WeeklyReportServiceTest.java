package com.devlog.service;

import com.devlog.domain.IssueStatus;
import com.devlog.domain.WorkLogStatus;
import com.devlog.dto.issue.IssueCreateRequest;
import com.devlog.dto.project.ProjectCreateRequest;
import com.devlog.dto.project.ProjectResponse;
import com.devlog.dto.report.WeeklyReportResponse;
import com.devlog.dto.todo.TodoCreateRequest;
import com.devlog.dto.worklog.WorkLogCreateRequest;
import com.devlog.dto.worklog.WorkLogResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WeeklyReportServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private TodoService todoService;

    @Autowired
    private WeeklyReportService weeklyReportService;

    @Test
    @DisplayName("generates weekly report text")
    void generateWeeklyReport() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("Public Maintenance", "Operation"));
        LocalDate startDate = LocalDate.of(2026, 7, 6);
        LocalDate endDate = LocalDate.of(2026, 7, 12);
        WorkLogResponse workLog = workLogService.create(new WorkLogCreateRequest(
                LocalDate.of(2026, 7, 7),
                "Analyze batch error",
                WorkLogStatus.DONE,
                "Root cause found and reprocessed",
                project.id()
        ));
        issueService.create(new IssueCreateRequest(
                "Batch failure resolved",
                "Night batch failed",
                "Null data",
                "Add validation",
                IssueStatus.RESOLVED,
                project.id(),
                workLog.id()
        ));
        issueService.create(new IssueCreateRequest(
                "Search slowdown",
                "Search response delayed",
                "Index missing",
                "",
                IssueStatus.OPEN,
                project.id(),
                null
        ));
        todoService.create(new TodoCreateRequest(
                LocalDate.of(2026, 7, 8),
                "Review search index",
                false,
                project.id()
        ));

        WeeklyReportResponse report = weeklyReportService.generate(startDate, endDate);

        assertThat(report.startDate()).isEqualTo(startDate);
        assertThat(report.endDate()).isEqualTo(endDate);
        assertThat(report.content()).contains("\uC8FC\uAC04\uBCF4\uACE0");
        assertThat(report.content()).contains("Analyze batch error");
        assertThat(report.content()).contains("\uD574\uACB0\uB41C \uC774\uC288");
        assertThat(report.content()).contains("Batch failure resolved");
        assertThat(report.content()).contains("\uB0A8\uC740 \uC774\uC288");
        assertThat(report.content()).contains("Search slowdown");
        assertThat(report.content()).contains("\uD560 \uC77C");
        assertThat(report.content()).contains("Review search index");
    }
}
