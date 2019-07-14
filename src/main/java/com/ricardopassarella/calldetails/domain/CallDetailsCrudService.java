package com.ricardopassarella.calldetails.domain;

import com.ricardopassarella.calldetails.dto.CallLog;
import com.ricardopassarella.calldetails.dto.CallerDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
class CallDetailsCrudService {

    private final CallDetailsRepository repository;

    @Transactional(readOnly = true)
    public List<CallerDetails> getCallerDetail(String callerId, LocalDateTime from, LocalDateTime to) {
        return repository.getCallerDetails(callerId, from, to);
    }

    @Transactional(readOnly = true)
    public List<CallLog> getCallLog(String callerId, LocalDateTime from, LocalDateTime to, int size, int page) {
        int offset = page * size;

        return repository.getCallLog(callerId, from, to, size, offset);
    }

    @Transactional(readOnly = true)
    public Integer getCallLogCount(String callerId, LocalDateTime from, LocalDateTime to) {
        return repository.getCountCallLog(callerId, from, to);
    }

}
