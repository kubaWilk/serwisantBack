package com.jakubwilk.serwisant.api.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class AuthControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> authenticationException(AuthenticationException exception){
        AuthErrorResponse error = new AuthErrorResponse(HttpStatus.FORBIDDEN.value(),
                exception.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
