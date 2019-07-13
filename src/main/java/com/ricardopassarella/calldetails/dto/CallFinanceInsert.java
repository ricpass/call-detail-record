package com.ricardopassarella.calldetails.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
public class CallFinanceInsert {

    private final BigDecimal cost;
    private final BigDecimal localCost;
    private final String localCurrency;
    private final BigDecimal exchangeRate;
    private final String callLogId;
}
