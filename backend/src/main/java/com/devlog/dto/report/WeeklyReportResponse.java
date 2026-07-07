package com.devlog.dto.report;

import java.time.LocalDate;

public record WeeklyReportResponse(
        LocalDate startDate,
        LocalDate endDate,
        String content
) {
}
