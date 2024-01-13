package com.jakubwilk.serwisant.api.controller.auth;

import com.jakubwilk.serwisant.api.controller.ErrorResponse;

public class AuthErrorResponse extends ErrorResponse {
    public AuthErrorResponse(int status, String message, long timeStamp) {
        super(status, message, timeStamp);
    }
}
