package com.example.e_commmerce.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(ApiException apiException){
        log.error("Exception occured", apiException);
        ExceptionResponse response = new ExceptionResponse(apiException.getMessage(),apiException.getHttpStatus().value(), LocalDateTime.now());
        return new ResponseEntity<>(response,apiException.getHttpStatus());
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        log.error("Exception occured general", exception);
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(),LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
