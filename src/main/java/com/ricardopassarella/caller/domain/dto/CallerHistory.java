package com.ricardopassarella.caller.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class CallerReportHistory {

    private final String recipient;
    private final LocalDateTime callStart;
    private final LocalDateTime callEnd;
}
