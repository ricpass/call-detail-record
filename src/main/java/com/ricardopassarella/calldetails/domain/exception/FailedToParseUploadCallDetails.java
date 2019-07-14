package com.ricardopassarella.calldetails.domain.exception;

public class FailedToParseUploadCallDetails extends RuntimeException {

    public FailedToParseUploadCallDetails(long line, Throwable cause) {
        super(String.format("Failed to parse csv data because of line %s", line), cause);
    }
}
