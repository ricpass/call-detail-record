package com.ricardopassarella.calldetails.dto.formatter;

import com.univocity.parsers.conversions.Conversion;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeFormatter implements Conversion<String, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeFormatter(String... pattern) {
        if (pattern.length != 1)
            throw new IllegalStateException("unknown args");

        this.formatter = DateTimeFormatter.ofPattern(pattern[0]);
    }

    @Override
    public LocalTime execute(String input) {
        return LocalTime.parse(input, formatter);
    }

    @Override
    public String revert(LocalTime input) {
        return formatter.format(input);
    }

}
