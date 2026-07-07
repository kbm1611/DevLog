package com.devlog.service;

import com.devlog.dto.project.ProjectCreateRequest;
import com.devlog.dto.project.ProjectResponse;
import com.devlog.dto.project.ProjectUpdateRequest;
import com.devlog.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    @DisplayName("프로젝트를 등록하고 목록에서 조회한다")
    void createAndFindProjects() {
        ProjectResponse created = projectService.create(new ProjectCreateRequest(
                "공공기관 유지보수 사업",
                "전자결재 유지보수와 기능 개선"
        ));

        List<ProjectResponse> projects = projectService.findAll();

        assertThat(created.id()).isNotNull();
        assertThat(projects).extracting(ProjectResponse::name)
                .contains("공공기관 유지보수 사업");
    }

    @Test
    @DisplayName("프로젝트 이름과 설명을 수정한다")
    void updateProject() {
        ProjectResponse created = projectService.create(new ProjectCreateRequest("내부 관리자", "초기 설명"));

        ProjectResponse updated = projectService.update(created.id(), new ProjectUpdateRequest(
                "내부 관리자 시스템 개발",
                "관리자 화면 MVP 개발"
        ));

        assertThat(updated.name()).isEqualTo("내부 관리자 시스템 개발");
        assertThat(updated.description()).isEqualTo("관리자 화면 MVP 개발");
    }

    @Test
    @DisplayName("프로젝트를 삭제하면 단건 조회에서 예외가 발생한다")
    void deleteProject() {
        ProjectResponse created = projectService.create(new ProjectCreateRequest("삭제 대상", "물리 삭제"));

        projectService.delete(created.id());

        assertThatThrownBy(() -> projectService.findById(created.id()))
                .isInstanceOf(NotFoundException.class);
    }
}
