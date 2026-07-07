package com.devlog.service;

import com.devlog.domain.Project;
import com.devlog.domain.WorkLog;
import com.devlog.dto.worklog.WorkLogCreateRequest;
import com.devlog.dto.worklog.WorkLogResponse;
import com.devlog.dto.worklog.WorkLogUpdateRequest;
import com.devlog.exception.NotFoundException;
import com.devlog.repository.WorkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkLogService {

    private final WorkLogRepository workLogRepository;
    private final ProjectService projectService;

    @Transactional
    public WorkLogResponse create(WorkLogCreateRequest request) {
        Project project = projectService.getProject(request.projectId());
        WorkLog workLog = new WorkLog(
                request.workDate(),
                request.title(),
                request.status(),
                request.memo(),
                project
        );
        return WorkLogResponse.from(workLogRepository.save(workLog));
    }

    public List<WorkLogResponse> findAll(LocalDate workDate, Long projectId) {
        return findAll(workDate, projectId, null, null);
    }

    public List<WorkLogResponse> findAll(LocalDate workDate, Long projectId, LocalDate startDate, LocalDate endDate) {
        return findByFilters(workDate, projectId, startDate, endDate).stream()
                .map(WorkLogResponse::from)
                .toList();
    }

    public WorkLogResponse findById(Long id) {
        return WorkLogResponse.from(getWorkLog(id));
    }

    @Transactional
    public WorkLogResponse update(Long id, WorkLogUpdateRequest request) {
        WorkLog workLog = getWorkLog(id);
        Project project = projectService.getProject(request.projectId());
        workLog.update(
                request.workDate(),
                request.title(),
                request.status(),
                request.memo(),
                project
        );
        return WorkLogResponse.from(workLog);
    }

    @Transactional
    public void delete(Long id) {
        WorkLog workLog = getWorkLog(id);
        workLogRepository.delete(workLog);
    }

    private List<WorkLog> findByFilters(LocalDate workDate, Long projectId, LocalDate startDate, LocalDate endDate) {
        if (startDate != null || endDate != null) {
            validateRange(startDate, endDate);
            if (projectId != null) {
                return workLogRepository.findByWorkDateBetweenAndProjectIdOrderByWorkDateDescIdDesc(
                        startDate,
                        endDate,
                        projectId
                );
            }
            return workLogRepository.findByWorkDateBetweenOrderByWorkDateDescIdDesc(startDate, endDate);
        }
        if (workDate != null && projectId != null) {
            return workLogRepository.findByWorkDateAndProjectIdOrderByIdDesc(workDate, projectId);
        }
        if (workDate != null) {
            return workLogRepository.findByWorkDateOrderByIdDesc(workDate);
        }
        if (projectId != null) {
            return workLogRepository.findByProjectIdOrderByIdDesc(projectId);
        }
        return workLogRepository.findAllByOrderByWorkDateDescIdDesc();
    }

    private void validateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate must be provided together.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before or equal to endDate.");
        }
    }

    public WorkLog getWorkLog(Long id) {
        return workLogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WorkLog not found. id=" + id));
    }
}
