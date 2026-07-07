package com.devlog.service;

import com.devlog.domain.Project;
import com.devlog.dto.project.ProjectCreateRequest;
import com.devlog.dto.project.ProjectResponse;
import com.devlog.dto.project.ProjectUpdateRequest;
import com.devlog.exception.NotFoundException;
import com.devlog.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponse create(ProjectCreateRequest request) {
        Project project = new Project(request.name(), request.description());
        return ProjectResponse.from(projectRepository.save(project));
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream()
                .map(ProjectResponse::from)
                .toList();
    }

    public ProjectResponse findById(Long id) {
        return ProjectResponse.from(getProject(id));
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectUpdateRequest request) {
        Project project = getProject(id);
        project.update(request.name(), request.description());
        return ProjectResponse.from(project);
    }

    @Transactional
    public void delete(Long id) {
        Project project = getProject(id);
        projectRepository.delete(project);
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found. id=" + id));
    }
}
