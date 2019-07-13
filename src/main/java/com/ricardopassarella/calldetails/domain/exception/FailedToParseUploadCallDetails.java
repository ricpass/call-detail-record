package com.ricardopassarella.calldetails.domain.exception;

public class FailedToParseUploadCallDetails extends RuntimeException {

    public FailedToParseUploadCallDetails(Throwable cause) {
        super("Failed to parse csv data", cause);
    }
}
