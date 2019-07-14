package com.ricardopassarella.caller;

import com.ricardopassarella.caller.domain.CallerFacade;
import com.ricardopassarella.caller.domain.dto.CallerHistory;
import com.ricardopassarella.caller.domain.dto.CallerReportResponse;
import com.ricardopassarella.common.PageableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/caller")
@RequiredArgsConstructor
@Slf4j
public class CallerController {

    private final CallerFacade facade;

    @GetMapping(value = "/{callerId}/report")
    ResponseEntity<CallerReportResponse> report(@PathVariable String callerId,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime from,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime to) {

        return facade.generateReport(callerId, from, to)
                     .map(report -> new ResponseEntity<>(report, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/{callerId}/history")
    ResponseEntity<PageableResponse<List<CallerHistory>>> history(@PathVariable String callerId,
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime from,
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime to,
                                                                  @RequestParam int size,
                                                                  @RequestParam int page) {
        var callLogHistory = facade.getCallLogHistory(callerId, from, to, size, page);

        return new ResponseEntity<>(callLogHistory, HttpStatus.OK);
    }
}
