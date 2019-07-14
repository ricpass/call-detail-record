package com.ricardopassarella.phonenumber.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
public class PhoneNumberHistory {

    private final String callerId;
    private final String recipient;
    private final LocalDateTime callStart;
    private final LocalDateTime callEnd;
}
