package com.jakubwilk.serwisant.api.controller.user;

import com.jakubwilk.serwisant.api.controller.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UserErrorResponse extends ErrorResponse {
    public UserErrorResponse(int status, String message, long timeStamp) {
        super(status, message, timeStamp);
    }
}
