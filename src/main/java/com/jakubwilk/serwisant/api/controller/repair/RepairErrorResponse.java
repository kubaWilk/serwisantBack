package com.jakubwilk.serwisant.api.controller.repair;

import com.jakubwilk.serwisant.api.controller.ErrorResponse;

public class RepairErrorResponse extends ErrorResponse {
    public RepairErrorResponse(int status, String message, long timeStamp) {
        super(status, message, timeStamp);
    }
}
