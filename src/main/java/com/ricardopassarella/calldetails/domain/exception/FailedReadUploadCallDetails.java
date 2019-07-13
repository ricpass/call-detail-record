package com.ricardopassarella.calldetails.domain.exception;

public class FailedReadUploadCallDetails extends RuntimeException {

    public FailedReadUploadCallDetails(Throwable cause) {
        super("Failed to read uploaded file", cause);
    }
}
