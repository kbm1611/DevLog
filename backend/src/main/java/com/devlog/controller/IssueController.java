package com.devlog.controller;

import com.devlog.domain.IssueStatus;
import com.devlog.dto.issue.IssueCreateRequest;
import com.devlog.dto.issue.IssueResponse;
import com.devlog.dto.issue.IssueUpdateRequest;
import com.devlog.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    @GetMapping
    public List<IssueResponse> findAll(
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) String keyword
    ) {
        return issueService.findAll(status, keyword);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse create(@Valid @RequestBody IssueCreateRequest request) {
        return issueService.create(request);
    }

    @GetMapping("/{id}")
    public IssueResponse findById(@PathVariable Long id) {
        return issueService.findById(id);
    }

    @PatchMapping("/{id}")
    public IssueResponse update(@PathVariable Long id, @Valid @RequestBody IssueUpdateRequest request) {
        return issueService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        issueService.delete(id);
    }
}
