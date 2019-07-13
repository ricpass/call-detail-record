package com.ricardopassarella.calldetails.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CallDetailsFacade {

    private final CallDetailsImportService importService;

    public void parseAndInsertCallDetails(MultipartFile multipartFile) {
        importService.parseAndInsertCallDetails(multipartFile);
    }

}
