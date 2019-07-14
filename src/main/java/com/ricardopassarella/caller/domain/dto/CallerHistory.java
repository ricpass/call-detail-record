package com.ricardopassarella.caller.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
public class CallerHistory {

    private final String recipient;
    private final LocalDateTime callStart;
    private final LocalDateTime callEnd;
}
