package com.ricardopassarella.calldetails.dto;

import com.ricardopassarella.calldetails.dto.formatter.LocalDateFormatter;
import com.ricardopassarella.calldetails.dto.formatter.LocalTimeFormatter;
import com.univocity.parsers.annotations.Convert;
import com.univocity.parsers.annotations.Parsed;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ToString
@RequiredArgsConstructor
public class CallDetailsUploadRow {

    @Parsed(field = "caller_id")
    private String callerId;

    @Parsed
    private String recipient;

    @Parsed(field = "call_date")
    @Convert(conversionClass = LocalDateFormatter.class, args = "dd/MM/yyyy")
    private LocalDate callDate;

    @Parsed(field = "end_time")
    @Convert(conversionClass = LocalTimeFormatter.class, args = "HH:mm:ss")
    private LocalTime endTime;

    @Parsed
    private int duration;

    @Parsed(field = "cost")
    private BigDecimal localCost;

    @Parsed
    private String reference;

    @Parsed
    private String currency;

}
