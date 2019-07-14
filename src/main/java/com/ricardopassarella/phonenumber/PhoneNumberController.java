package com.ricardopassarella.phonenumber;

import com.ricardopassarella.common.PageableResponse;
import com.ricardopassarella.phonenumber.domain.PhoneNmberFacade;
import com.ricardopassarella.phonenumber.domain.dto.PhoneNumberHistory;
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
@RequestMapping("/phonenumber")
@RequiredArgsConstructor
@Slf4j
public class PhoneNumberController {

    private final PhoneNmberFacade facade;

    @GetMapping(value = "/{phonenumber}/history")
    ResponseEntity<PageableResponse<List<PhoneNumberHistory>>> history(@PathVariable String phonenumber,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime from,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime to,
                                                                       @RequestParam int size,
                                                                       @RequestParam int page) {
        var callLogHistory = facade.getCallLogHistory(phonenumber, from, to, size, page);

        return new ResponseEntity<>(callLogHistory, HttpStatus.OK);
    }
}
