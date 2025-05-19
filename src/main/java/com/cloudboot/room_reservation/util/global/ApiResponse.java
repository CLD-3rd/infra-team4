package com.cloudboot.room_reservation.util.global;

import lombok.Getter;

@Getter
public class ApiResponse<T>{

    private final T data;
    private final String message;
    private final String errorCode;

    protected ApiResponse(T data, String message, String errorCode) {
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(null, "SUCCESS", null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, "SUCCESS", null);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(null, message, errorCode);
    }

}
