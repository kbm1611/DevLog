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
