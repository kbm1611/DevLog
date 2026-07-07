package com.devlog.service;

import com.devlog.domain.Issue;
import com.devlog.domain.IssueStatus;
import com.devlog.domain.Todo;
import com.devlog.domain.WorkLog;
import com.devlog.dto.report.WeeklyReportResponse;
import com.devlog.repository.IssueRepository;
import com.devlog.repository.TodoRepository;
import com.devlog.repository.WorkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeeklyReportService {

    private static final String TITLE = "\uC8FC\uAC04\uBCF4\uACE0";
    private static final String WORK_LOGS = "\uC5C5\uBB34 \uC9C4\uD589 \uB0B4\uC6A9";
    private static final String RESOLVED_ISSUES = "\uD574\uACB0\uB41C \uC774\uC288";
    private static final String REMAINING_ISSUES = "\uB0A8\uC740 \uC774\uC288";
    private static final String TODOS = "\uD560 \uC77C";
    private static final String DONE = "\uC644\uB8CC";
    private static final String NOT_DONE = "\uBBF8\uC644\uB8CC";
    private static final String CAUSE = "\uC6D0\uC778";

    private final WorkLogRepository workLogRepository;
    private final IssueRepository issueRepository;
    private final TodoRepository todoRepository;

    public WeeklyReportResponse generate(LocalDate startDate, LocalDate endDate) {
        validateRange(startDate, endDate);

        List<WorkLog> workLogs = workLogRepository.findByWorkDateBetweenOrderByWorkDateAscIdAsc(startDate, endDate);
        List<Issue> issues = issueRepository.findByCreatedAtBetween(
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX)
        );
        List<Todo> todos = todoRepository.findByTodoDateBetweenOrderByTodoDateAscIdAsc(startDate, endDate);

        String content = buildContent(startDate, endDate, workLogs, issues, todos);
        return new WeeklyReportResponse(startDate, endDate, content);
    }

    private void validateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate are required.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before or equal to endDate.");
        }
    }

    private String buildContent(LocalDate startDate, LocalDate endDate, List<WorkLog> workLogs, List<Issue> issues, List<Todo> todos) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(TITLE).append("] ").append(startDate).append(" ~ ").append(endDate).append("\n\n");

        appendWorkLogs(builder, workLogs);
        appendResolvedIssues(builder, issues);
        appendRemainingIssues(builder, issues);
        appendTodos(builder, todos);

        return builder.toString().trim();
    }

    private void appendWorkLogs(StringBuilder builder, List<WorkLog> workLogs) {
        builder.append("1. ").append(WORK_LOGS).append("\n");
        if (workLogs.isEmpty()) {
            builder.append("- No work logs.\n\n");
            return;
        }
        for (WorkLog workLog : workLogs) {
            builder.append("- ")
                    .append(workLog.getWorkDate())
                    .append(" [")
                    .append(workLog.getProject().getName())
                    .append("] ")
                    .append(workLog.getTitle())
                    .append(" (")
                    .append(workLog.getStatus())
                    .append(")");
            if (hasText(workLog.getMemo())) {
                builder.append(" - ").append(workLog.getMemo());
            }
            builder.append("\n");
        }
        builder.append("\n");
    }

    private void appendResolvedIssues(StringBuilder builder, List<Issue> issues) {
        builder.append("2. ").append(RESOLVED_ISSUES).append("\n");
        List<Issue> resolvedIssues = issues.stream()
                .filter(issue -> issue.getStatus() == IssueStatus.RESOLVED)
                .toList();
        if (resolvedIssues.isEmpty()) {
            builder.append("- No resolved issues.\n\n");
            return;
        }
        for (Issue issue : resolvedIssues) {
            builder.append("- [")
                    .append(issue.getProject().getName())
                    .append("] ")
                    .append(issue.getTitle());
            if (hasText(issue.getSolution())) {
                builder.append(" - ").append(issue.getSolution());
            }
            builder.append("\n");
        }
        builder.append("\n");
    }

    private void appendRemainingIssues(StringBuilder builder, List<Issue> issues) {
        builder.append("3. ").append(REMAINING_ISSUES).append("\n");
        List<Issue> remainingIssues = issues.stream()
                .filter(issue -> issue.getStatus() != IssueStatus.RESOLVED)
                .toList();
        if (remainingIssues.isEmpty()) {
            builder.append("- No remaining issues.\n\n");
            return;
        }
        for (Issue issue : remainingIssues) {
            builder.append("- [")
                    .append(issue.getStatus())
                    .append("] [")
                    .append(issue.getProject().getName())
                    .append("] ")
                    .append(issue.getTitle());
            if (hasText(issue.getCause())) {
                builder.append(" - ").append(CAUSE).append(": ").append(issue.getCause());
            }
            builder.append("\n");
        }
        builder.append("\n");
    }

    private void appendTodos(StringBuilder builder, List<Todo> todos) {
        builder.append("4. ").append(TODOS).append("\n");
        if (todos.isEmpty()) {
            builder.append("- No todos.\n");
            return;
        }
        for (Todo todo : todos) {
            builder.append("- ")
                    .append(todo.getTodoDate())
                    .append(" [")
                    .append(todo.isCompleted() ? DONE : NOT_DONE)
                    .append("] ");
            if (todo.getProject() != null) {
                builder.append("[")
                        .append(todo.getProject().getName())
                        .append("] ");
            }
            builder.append(todo.getContent()).append("\n");
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
