package com.ricardopassarella.phonenumber.domain;

import com.ricardopassarella.common.PageableResponse;
import com.ricardopassarella.phonenumber.domain.dto.PhoneNumberHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneNmberFacade {

    private final PhoneNumberHistoryService historyService;

    public PageableResponse<List<PhoneNumberHistory>> getCallLogHistory(String phonenumber, LocalDateTime from, LocalDateTime to, int size, int page) {
        return historyService.getCallLogHistory(phonenumber, from, to, size, page);
    }
}
