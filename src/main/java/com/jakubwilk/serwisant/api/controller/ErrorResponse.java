package com.jakubwilk.serwisant.api.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class ErrorResponse {
    protected int status;
    protected String message;
    protected long timeStamp;
}
