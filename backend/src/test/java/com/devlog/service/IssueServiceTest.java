package com.devlog.service;

import com.devlog.domain.IssueStatus;
import com.devlog.domain.WorkLogStatus;
import com.devlog.dto.issue.IssueCreateRequest;
import com.devlog.dto.issue.IssueResponse;
import com.devlog.dto.issue.IssueUpdateRequest;
import com.devlog.dto.project.ProjectCreateRequest;
import com.devlog.dto.project.ProjectResponse;
import com.devlog.dto.worklog.WorkLogCreateRequest;
import com.devlog.dto.worklog.WorkLogResponse;
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
public class IssueServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private IssueService issueService;

    @Test
    @DisplayName("creates and filters issues by status and keyword")
    void createAndFilterIssues() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("Public Maintenance", "Operation"));
        WorkLogResponse workLog = workLogService.create(new WorkLogCreateRequest(
                LocalDate.of(2026, 7, 7),
                "Analyze logs",
                WorkLogStatus.IN_PROGRESS,
                "Checked batch logs",
                project.id()
        ));
        issueService.create(new IssueCreateRequest(
                "Batch failure",
                "Night batch failed",
                "Null data",
                "Add validation",
                IssueStatus.OPEN,
                project.id(),
                workLog.id()
        ));
        issueService.create(new IssueCreateRequest(
                "Screen bug",
                "Search screen fails",
                "Missing parameter",
                "Handle default value",
                IssueStatus.RESOLVED,
                project.id(),
                null
        ));

        List<IssueResponse> results = issueService.findAll(IssueStatus.OPEN, "Batch");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).title()).isEqualTo("Batch failure");
        assertThat(results.get(0).projectName()).isEqualTo("Public Maintenance");
        assertThat(results.get(0).workLogId()).isEqualTo(workLog.id());
    }

    @Test
    @DisplayName("updates an issue")
    void updateIssue() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("Admin System", "Development"));
        IssueResponse created = issueService.create(new IssueCreateRequest(
                "Login error",
                "Login fails",
                "Expired session handling missing",
                "",
                IssueStatus.OPEN,
                project.id(),
                null
        ));

        IssueResponse updated = issueService.update(created.id(), new IssueUpdateRequest(
                "Login error resolved",
                "Login failure case handled",
                "Expired session handling missing",
                "Redirect to login page",
                IssueStatus.RESOLVED,
                project.id(),
                null
        ));

        assertThat(updated.title()).isEqualTo("Login error resolved");
        assertThat(updated.status()).isEqualTo(IssueStatus.RESOLVED);
        assertThat(updated.solution()).isEqualTo("Redirect to login page");
    }

    @Test
    @DisplayName("deletes an issue")
    void deleteIssue() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("Delete Project", "Delete"));
        IssueResponse created = issueService.create(new IssueCreateRequest(
                "Issue to delete",
                "Delete target",
                "",
                "",
                IssueStatus.HOLD,
                project.id(),
                null
        ));

        issueService.delete(created.id());

        assertThatThrownBy(() -> issueService.findById(created.id()))
                .isInstanceOf(NotFoundException.class);
    }
}
