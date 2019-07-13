package com.ricardopassarella.calldetails.domain.exception;

public class FailedToInsertCallDetails extends RuntimeException {

    public FailedToInsertCallDetails(Throwable cause) {
        super("Failed to insert call details on the database", cause);
    }
}
