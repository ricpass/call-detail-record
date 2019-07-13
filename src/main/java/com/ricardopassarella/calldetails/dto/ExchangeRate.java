package com.ricardopassarella.calldetails.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
public class ExchangeRate {

    private final String localCurrency;
    private final BigDecimal exchangeRate;
}
