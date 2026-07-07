package com.devlog.controller;

import com.devlog.dto.report.WeeklyReportResponse;
import com.devlog.service.WeeklyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;

    @GetMapping("/weekly")
    public WeeklyReportResponse generateWeeklyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return weeklyReportService.generate(startDate, endDate);
    }
}
