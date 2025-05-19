package com.cloudboot.room_reservation.util.exception;

import lombok.Data;

@Data
public class ErrorResult {

    private String errorCode;

    private String errorMessage;

    public ErrorResult() {
    }

    public ErrorResult(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
