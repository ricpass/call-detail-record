package com.ricardopassarella.caller.domain;

import com.ricardopassarella.caller.domain.dto.CallerHistory;
import com.ricardopassarella.caller.domain.dto.CallerReportResponse;
import com.ricardopassarella.common.PageableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallerFacade {

    private final CallerReportService reportService;
    private final CallerHistoryService historyService;

    public Optional<CallerReportResponse> generateReport(String callerId, LocalDateTime from, LocalDateTime to) {
        return reportService.generateReport(callerId, from, to);
    }

    public PageableResponse<List<CallerHistory>> getCallLogHistory(String callerId, LocalDateTime from, LocalDateTime to, int size, int page) {
        return historyService.getCallLogHistory(callerId, from, to, size, page);
    }

}
