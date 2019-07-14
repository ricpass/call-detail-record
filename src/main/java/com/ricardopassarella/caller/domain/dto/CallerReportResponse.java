package com.ricardopassarella.caller.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
public class CallerReportResponse {

    private final BigDecimal averageDurationInSeconds;
    private final BigDecimal medianOfDurationInSeconds;
    private final long longerCallInSeconds;
    private final long callVolume;
    private final BigDecimal totalCost;
    private final String currency;
}
