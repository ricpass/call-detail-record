package com.ricardopassarella.calldetails;

import com.ricardopassarella.calldetails.domain.CallDetailsFacade;
import com.ricardopassarella.calldetails.domain.exception.FailedReadUploadCallDetails;
import com.ricardopassarella.calldetails.domain.exception.FailedToInsertCallDetails;
import com.ricardopassarella.calldetails.domain.exception.FailedToParseUploadCallDetails;
import com.ricardopassarella.calldetails.domain.exception.UnknownCurrencyException;
import com.ricardopassarella.common.ErrorResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/call-details")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Call Details")
public class CallDetailsController {

    private final CallDetailsFacade callDetailsFacade;

    @PostMapping(value = "/upload")
    ResponseEntity<Void> upload(@RequestParam("csv") MultipartFile multipartFile) {
        callDetailsFacade.parseAndInsertCallDetails(multipartFile);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = FailedReadUploadCallDetails.class)
    ErrorResponse handleFailedReadUploadCallDetails(Throwable error) {
        log.debug(error.getMessage(), error);
        return new ErrorResponse(error.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = FailedToParseUploadCallDetails.class)
    ErrorResponse handleFailedToParseUploadCallDetails(Throwable error) {
        log.debug(error.getMessage(), error);
        return new ErrorResponse(error.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = FailedToInsertCallDetails.class)
    ErrorResponse handleFailedToInsertCallDetails(Throwable error) {
        log.debug(error.getMessage(), error);
        return new ErrorResponse(error.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = UnknownCurrencyException.class)
    ErrorResponse handleUnknownCurrencyException(Throwable error) {
        log.debug(error.getMessage(), error);
        return new ErrorResponse(error.getMessage());
    }

}
