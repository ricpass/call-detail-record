package com.ricardopassarella.calldetails.domain;

import com.ricardopassarella.calldetails.dto.CallLog;
import com.ricardopassarella.calldetails.dto.CallerDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallDetailsFacade {

    private final CallDetailsImportService importService;
    private final CallDetailsCrudService crudService;

    public void parseAndInsertCallDetails(MultipartFile multipartFile) {
        importService.parseAndInsertCallDetails(multipartFile);
    }

    public List<CallerDetails> getCallerDetail(String callerId, LocalDateTime from, LocalDateTime to) {
        return crudService.getCallerDetail(callerId, from, to);
    }

    public List<CallLog> getCallLogByCaller(String callerId, LocalDateTime from, LocalDateTime to, int size, int page) {
        return crudService.getCallLogByCaller(callerId, from, to, size, page);
    }

    public Integer getCallLogCountByCaller(String callerId, LocalDateTime from, LocalDateTime to) {
        return crudService.getCallLogCountByCaller(callerId, from, to);
    }

    public List<CallLog> getCallLogByPhoneNumber(String phoneNumber, LocalDateTime from, LocalDateTime to, int size, int page) {
        return crudService.getCallLogByPhoneNumber(phoneNumber, from, to, size, page);
    }

    public Integer getCallLogCountByPhoneNumber(String phoneNumber, LocalDateTime from, LocalDateTime to) {
        return crudService.getCallLogCountByPhoneNumber(phoneNumber, from, to);
    }
}
