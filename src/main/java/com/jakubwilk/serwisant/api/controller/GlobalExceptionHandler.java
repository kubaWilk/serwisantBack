package com.jakubwilk.serwisant.api.controller;

import com.jakubwilk.serwisant.api.controller.auth.AuthErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> nullPointer(NullPointerException exception){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_MODIFIED.value(),
                exception.getMessage(),
                System.currentTimeMillis()) {
        };

        return new ResponseEntity<>(error, HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException exception){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> illegalArgument(IllegalArgumentException exception){
        AuthErrorResponse error = new AuthErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
