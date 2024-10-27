package com.backend_challenge.backendChallenge.controller;

import com.backend_challenge.backendChallenge.dtos.BookReportDto;
import com.backend_challenge.backendChallenge.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/rented")
    public ResponseEntity<List<BookReportDto>> getRentedReports() {

        return ResponseEntity.ok(reportService.generateMonthlyRentedReports());
    }

    @GetMapping("/delayed")
    public ResponseEntity<List<BookReportDto>> getDelayedAReports() {

        return ResponseEntity.ok(reportService.generateMonthlyDelayedReports());
    }


}
