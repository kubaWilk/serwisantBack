package com.jakubwilk.serwisant.api.controller.device;

import com.jakubwilk.serwisant.api.exception.DeviceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DeviceExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<DeviceErrorResponse> deviceNotFound(DeviceNotFoundException exception){
        DeviceErrorResponse error = new DeviceErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
