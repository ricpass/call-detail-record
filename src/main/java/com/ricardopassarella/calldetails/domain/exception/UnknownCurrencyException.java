package com.ricardopassarella.calldetails.domain.exception;

public class UnknownCurrencyException extends RuntimeException {

    public UnknownCurrencyException(String currency, String reference) {
        super(String.format("Currency '%s' not supported with reference %s", currency, reference));
    }
}
