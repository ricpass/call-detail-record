package com.ricardopassarella.calldetails.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class CallerDetails {

    private final LocalDateTime callStart;
    private final LocalDateTime callEnd;
    private final BigDecimal cost;

}
