package com.ricardopassarella.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class MainControllerAdvice {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    ErrorResponse handleAnyException(Throwable error) {
        log.error("Internal server error occurred", error);
        return new ErrorResponse("Internal server error");
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ErrorResponse handleMissingServletRequestParameterException(Throwable error) {
        log.debug("Internal server error occurred", error);
        return new ErrorResponse("Invalid request");
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    ErrorResponse handleMissingServletRequestParameterException(Exception exception) {
        log.debug("Invalid input", exception);
        return new ErrorResponse(exception.getMessage());
    }

}
