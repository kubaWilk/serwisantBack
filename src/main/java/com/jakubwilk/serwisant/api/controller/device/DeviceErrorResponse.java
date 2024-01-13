package com.jakubwilk.serwisant.api.controller.device;

import com.jakubwilk.serwisant.api.controller.ErrorResponse;

public class DeviceErrorResponse extends ErrorResponse {
    public DeviceErrorResponse(int status, String message, long timeStamp) {
        super(status, message, timeStamp);
    }
}
