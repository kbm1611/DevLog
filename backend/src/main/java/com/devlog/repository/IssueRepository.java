package com.devlog.repository;

import com.devlog.domain.Issue;
import com.devlog.domain.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("""
            select i
            from Issue i
            join fetch i.project
            left join fetch i.workLog
            where (:status is null or i.status = :status)
              and (:keyword is null
                   or lower(i.title) like lower(concat('%', :keyword, '%'))
                   or lower(i.content) like lower(concat('%', :keyword, '%'))
                   or lower(coalesce(i.cause, '')) like lower(concat('%', :keyword, '%'))
                   or lower(coalesce(i.solution, '')) like lower(concat('%', :keyword, '%')))
            order by i.id desc
            """)
    List<Issue> search(@Param("status") IssueStatus status, @Param("keyword") String keyword);

    @Query("""
            select i
            from Issue i
            join fetch i.project
            left join fetch i.workLog
            where i.createdAt between :startDateTime and :endDateTime
            order by i.status asc, i.id desc
            """)
    List<Issue> findByCreatedAtBetween(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}
