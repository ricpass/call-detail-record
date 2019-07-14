package com.ricardopassarella.calldetails.domain;

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
}
