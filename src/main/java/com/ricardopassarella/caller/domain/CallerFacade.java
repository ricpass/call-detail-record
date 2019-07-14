package com.ricardopassarella.caller.domain;

import com.ricardopassarella.caller.domain.dto.CallerReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallerFacade {

    private final CallerReportService reportService;

    public Optional<CallerReportResponse> generateReport(String callerId, LocalDateTime from, LocalDateTime to) {
        return reportService.generateReport(callerId, from, to);
    }

}
