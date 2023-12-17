package com.jakubwilk.serwisant.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> userNotFoundHandler(UserNotFoundException exception) {
        UserErrorResponse error = new UserErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> userAlreadyExists(UserAlreadyExistsException exception) {
        UserErrorResponse error = new UserErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> illegalArgument(IllegalArgumentException exception) {
        UserErrorResponse error = new UserErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }
}
