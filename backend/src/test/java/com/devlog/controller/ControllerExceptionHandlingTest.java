package com.devlog.controller;

import com.devlog.exception.NotFoundException;
import com.devlog.service.IssueService;
import com.devlog.service.ProjectService;
import com.devlog.service.TodoService;
import com.devlog.service.WeeklyReportService;
import com.devlog.service.WorkLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        IssueController.class,
        ProjectController.class,
        TodoController.class,
        WorkLogController.class,
        WeeklyReportController.class
})
class ControllerExceptionHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private IssueService issueService;

    @MockitoBean
    private TodoService todoService;

    @MockitoBean
    private WorkLogService workLogService;

    @MockitoBean
    private WeeklyReportService weeklyReportService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("returns a consistent 404 error response when a project is missing")
    void projectNotFoundReturnsErrorResponse() throws Exception {
        when(projectService.findById(99L))
                .thenThrow(new NotFoundException("Project not found. id=99"));

        mockMvc.perform(get("/api/projects/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Project not found. id=99"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("rejects a blank project name before calling the service")
    void blankProjectNameReturnsValidationError() throws Exception {
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "   ",
                                  "description": "missing name"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("name")))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(projectService, never()).create(any());
    }

    @Test
    @DisplayName("rejects missing required work log fields before calling the service")
    void missingWorkLogRequiredFieldsReturnsValidationError() throws Exception {
        mockMvc.perform(post("/api/work-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "memo": "missing required fields"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(workLogService, never()).create(any());
    }

    @Test
    @DisplayName("rejects missing required issue fields before calling the service")
    void missingIssueRequiredFieldsReturnsValidationError() throws Exception {
        mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "content": "",
                                  "cause": "missing required fields"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(issueService, never()).create(any());
    }

    @Test
    @DisplayName("rejects missing required todo fields before calling the service")
    void missingTodoRequiredFieldsReturnsValidationError() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(todoService, never()).create(any());
    }

    @Test
    @DisplayName("returns a consistent 400 error response for an invalid work log status")
    void invalidWorkLogStatusReturnsErrorResponse() throws Exception {
        mockMvc.perform(post("/api/work-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workDate": "2026-07-07",
                                  "title": "Status validation",
                                  "status": "WAITING",
                                  "memo": "invalid enum",
                                  "projectId": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(workLogService, never()).create(any());
    }

    @Test
    @DisplayName("returns a consistent 400 error response for invalid weekly report dates")
    void invalidWeeklyReportDateRangeReturnsErrorResponse() throws Exception {
        when(weeklyReportService.generate(eq(LocalDate.of(2026, 7, 8)), eq(LocalDate.of(2026, 7, 7))))
                .thenThrow(new IllegalArgumentException("startDate must be before or equal to endDate."));

        mockMvc.perform(get("/api/reports/weekly")
                        .param("startDate", "2026-07-08")
                        .param("endDate", "2026-07-07"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("startDate must be before or equal to endDate."))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
