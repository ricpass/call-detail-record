package com.ricardopassarella.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class PageableResponse<T> {

    private final T data;
    private final int total;
}
