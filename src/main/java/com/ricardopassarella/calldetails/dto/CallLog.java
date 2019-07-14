package com.ricardopassarella.calldetails.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class CallLogInsert {

    private final String uuid;
    private final String callerId;
    private final String recipient;
    private final LocalDateTime callStart;
    private final LocalDateTime callEnd;
    private final String reference;
}
