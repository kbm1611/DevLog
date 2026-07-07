package com.devlog.service;

import com.devlog.domain.WorkLogStatus;
import com.devlog.dto.project.ProjectCreateRequest;
import com.devlog.dto.project.ProjectResponse;
import com.devlog.dto.worklog.WorkLogCreateRequest;
import com.devlog.dto.worklog.WorkLogResponse;
import com.devlog.dto.worklog.WorkLogUpdateRequest;
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
public class WorkLogServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkLogService workLogService;

    @Test
    @DisplayName("업무 일지를 등록하고 날짜와 프로젝트로 필터링한다")
    void createAndFilterWorkLogs() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("공공기관 유지보수", "운영 업무"));
        LocalDate workDate = LocalDate.of(2026, 7, 7);

        workLogService.create(new WorkLogCreateRequest(
                workDate,
                "배치 오류 원인 분석",
                WorkLogStatus.IN_PROGRESS,
                "운영 배치 로그 확인",
                project.id()
        ));
        workLogService.create(new WorkLogCreateRequest(
                workDate.plusDays(1),
                "다른 날짜 업무",
                WorkLogStatus.PLANNED,
                "내일 처리",
                project.id()
        ));

        List<WorkLogResponse> results = workLogService.findAll(workDate, project.id());

        assertThat(results).hasSize(1);
        assertThat(results.get(0).title()).isEqualTo("배치 오류 원인 분석");
        assertThat(results.get(0).projectName()).isEqualTo("공공기관 유지보수");
    }

    @Test
    @DisplayName("업무 일지를 시작일과 종료일 범위로 필터링한다")
    void findWorkLogsByDateRange() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("범위 프로젝트", "범위 테스트"));
        LocalDate startDate = LocalDate.of(2026, 7, 7);
        LocalDate endDate = LocalDate.of(2026, 7, 8);

        workLogService.create(new WorkLogCreateRequest(
                startDate.minusDays(1),
                "범위 이전 업무",
                WorkLogStatus.DONE,
                "제외",
                project.id()
        ));
        workLogService.create(new WorkLogCreateRequest(
                startDate,
                "범위 시작 업무",
                WorkLogStatus.IN_PROGRESS,
                "포함",
                project.id()
        ));
        workLogService.create(new WorkLogCreateRequest(
                endDate,
                "범위 종료 업무",
                WorkLogStatus.PLANNED,
                "포함",
                project.id()
        ));
        workLogService.create(new WorkLogCreateRequest(
                endDate.plusDays(1),
                "범위 이후 업무",
                WorkLogStatus.BLOCKED,
                "제외",
                project.id()
        ));

        List<WorkLogResponse> results = workLogService.findAll(null, null, startDate, endDate);

        assertThat(results)
                .extracting(WorkLogResponse::title)
                .containsExactly("범위 종료 업무", "범위 시작 업무");
    }

    @Test
    @DisplayName("업무 일지 날짜 범위와 프로젝트 필터를 함께 적용한다")
    void findWorkLogsByDateRangeAndProject() {
        ProjectResponse backend = projectService.create(new ProjectCreateRequest("Backend", "API"));
        ProjectResponse frontend = projectService.create(new ProjectCreateRequest("Frontend", "UI"));
        LocalDate startDate = LocalDate.of(2026, 7, 7);
        LocalDate endDate = LocalDate.of(2026, 7, 9);

        workLogService.create(new WorkLogCreateRequest(
                startDate,
                "Backend 범위 업무",
                WorkLogStatus.DONE,
                "포함",
                backend.id()
        ));
        workLogService.create(new WorkLogCreateRequest(
                startDate.plusDays(1),
                "Frontend 범위 업무",
                WorkLogStatus.DONE,
                "다른 프로젝트",
                frontend.id()
        ));
        workLogService.create(new WorkLogCreateRequest(
                endDate.plusDays(1),
                "Backend 범위 밖 업무",
                WorkLogStatus.DONE,
                "범위 밖",
                backend.id()
        ));

        List<WorkLogResponse> results = workLogService.findAll(null, backend.id(), startDate, endDate);

        assertThat(results)
                .extracting(WorkLogResponse::title)
                .containsExactly("Backend 범위 업무");
    }

    @Test
    @DisplayName("업무 일지 내용을 수정한다")
    void updateWorkLog() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("내부 관리자", "개발"));
        WorkLogResponse created = workLogService.create(new WorkLogCreateRequest(
                LocalDate.of(2026, 7, 7),
                "목록 API 구현",
                WorkLogStatus.PLANNED,
                "컨트롤러 작성 전",
                project.id()
        ));

        WorkLogResponse updated = workLogService.update(created.id(), new WorkLogUpdateRequest(
                LocalDate.of(2026, 7, 7),
                "목록 API 구현 완료",
                WorkLogStatus.DONE,
                "서비스 테스트 통과",
                project.id()
        ));

        assertThat(updated.title()).isEqualTo("목록 API 구현 완료");
        assertThat(updated.status()).isEqualTo(WorkLogStatus.DONE);
        assertThat(updated.memo()).isEqualTo("서비스 테스트 통과");
    }

    @Test
    @DisplayName("업무 일지를 삭제하면 단건 조회에서 예외가 발생한다")
    void deleteWorkLog() {
        ProjectResponse project = projectService.create(new ProjectCreateRequest("삭제 프로젝트", "삭제 테스트"));
        WorkLogResponse created = workLogService.create(new WorkLogCreateRequest(
                LocalDate.of(2026, 7, 7),
                "삭제할 업무",
                WorkLogStatus.BLOCKED,
                "삭제 예정",
                project.id()
        ));

        workLogService.delete(created.id());

        assertThatThrownBy(() -> workLogService.findById(created.id()))
                .isInstanceOf(NotFoundException.class);
    }
}
