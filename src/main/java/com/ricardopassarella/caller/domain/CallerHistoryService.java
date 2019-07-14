package com.ricardopassarella.caller.domain;

import com.ricardopassarella.calldetails.domain.CallDetailsFacade;
import com.ricardopassarella.caller.domain.dto.CallerHistory;
import com.ricardopassarella.common.PageableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallerHistoryService {

    private final CallDetailsFacade callDetailsFacade;

    PageableResponse<List<CallerHistory>> getCallLogHistory(String callerId, LocalDateTime from, LocalDateTime to, int size, int page) {
        List<CallerHistory> history = callDetailsFacade.getCallLogByCaller(callerId, from, to, size, page)
                                                       .stream()
                                                       .map(callLog -> CallerHistory.builder()
                                                                                    .callStart(callLog.getCallStart())
                                                                                    .callEnd(callLog.getCallEnd())
                                                                                    .recipient(callLog.getRecipient())
                                                                                    .build())
                                                       .collect(Collectors.toList());

        Integer callLogCount = callDetailsFacade.getCallLogCountByCaller(callerId, from, to);

        return new PageableResponse<>(history, callLogCount);
    }
}
