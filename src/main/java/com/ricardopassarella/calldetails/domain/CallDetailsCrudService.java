package com.ricardopassarella.calldetails.domain;

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
}
