package com.ricardopassarella.calldetails.dto.formatter;

import com.univocity.parsers.conversions.Conversion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateFormatter implements Conversion<String, LocalDate> {

    private final DateTimeFormatter formatter;

    public LocalDateFormatter(String... pattern) {
        if (pattern.length != 1) {
            throw new IllegalStateException("unknown args");
        }

        this.formatter = DateTimeFormatter.ofPattern(pattern[0]);
    }

    @Override
    public LocalDate execute(String input) {
        return LocalDate.parse(input, formatter);
    }

    @Override
    public String revert(LocalDate input) {
        return formatter.format(input);
    }

}
