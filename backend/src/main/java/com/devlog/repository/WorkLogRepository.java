package com.devlog.repository;

import com.devlog.domain.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    List<WorkLog> findByWorkDateOrderByIdDesc(LocalDate workDate);

    List<WorkLog> findByProjectIdOrderByIdDesc(Long projectId);

    List<WorkLog> findByWorkDateAndProjectIdOrderByIdDesc(LocalDate workDate, Long projectId);

    List<WorkLog> findByWorkDateBetweenOrderByWorkDateDescIdDesc(LocalDate startDate, LocalDate endDate);

    List<WorkLog> findByWorkDateBetweenAndProjectIdOrderByWorkDateDescIdDesc(
            LocalDate startDate,
            LocalDate endDate,
            Long projectId
    );

    List<WorkLog> findAllByOrderByWorkDateDescIdDesc();

    List<WorkLog> findByWorkDateBetweenOrderByWorkDateAscIdAsc(LocalDate startDate, LocalDate endDate);
}
