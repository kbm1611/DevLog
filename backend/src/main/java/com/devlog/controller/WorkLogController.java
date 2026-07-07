package com.devlog.controller;

import com.devlog.dto.worklog.WorkLogCreateRequest;
import com.devlog.dto.worklog.WorkLogResponse;
import com.devlog.dto.worklog.WorkLogUpdateRequest;
import com.devlog.service.WorkLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-logs")
public class WorkLogController {

    private final WorkLogService workLogService;

    @GetMapping
    public List<WorkLogResponse> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long projectId
    ) {
        return workLogService.findAll(date, projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkLogResponse create(@Valid @RequestBody WorkLogCreateRequest request) {
        return workLogService.create(request);
    }

    @GetMapping("/{id}")
    public WorkLogResponse findById(@PathVariable Long id) {
        return workLogService.findById(id);
    }

    @PatchMapping("/{id}")
    public WorkLogResponse update(@PathVariable Long id, @Valid @RequestBody WorkLogUpdateRequest request) {
        return workLogService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        workLogService.delete(id);
    }
}
