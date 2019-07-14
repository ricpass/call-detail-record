package com.ricardopassarella.calldetails.domain.exception;

public class FailedToInsertCallDetails extends RuntimeException {

    public FailedToInsertCallDetails(Throwable throwable) {
        super("Failed to insert call details on the database with cause: " + throwable.getCause(), throwable);
    }
}
