package com.jakubwilk.serwisant.api.controller.repair;

import com.jakubwilk.serwisant.api.exception.RepairNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RepairExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<RepairErrorResponse> repairNotFoundHandler(RepairNotFoundException exception){
        RepairErrorResponse error = new RepairErrorResponse(HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
