package com.ricardopassarella.phonenumber.domain;

import com.ricardopassarella.calldetails.domain.CallDetailsFacade;
import com.ricardopassarella.common.PageableResponse;
import com.ricardopassarella.phonenumber.domain.dto.PhoneNumberHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhoneNumberHistoryService {

    private final CallDetailsFacade callDetailsFacade;

    PageableResponse<List<PhoneNumberHistory>> getCallLogHistory(String phoneNumber, LocalDateTime from, LocalDateTime to, int size, int page) {
        List<PhoneNumberHistory> history = callDetailsFacade.getCallLogByPhoneNumber(phoneNumber, from, to, size, page)
                                                            .stream()
                                                            .map(callLog -> PhoneNumberHistory.builder()
                                                                                              .callerId(callLog.getCallerId())
                                                                                              .callStart(callLog.getCallStart())
                                                                                              .callEnd(callLog.getCallEnd())
                                                                                              .recipient(callLog.getRecipient())
                                                                                              .build())
                                                            .collect(Collectors.toList());

        Integer callLogCount = callDetailsFacade.getCallLogCountByPhoneNumber(phoneNumber, from, to);

        return new PageableResponse<>(history, callLogCount);
    }
}
