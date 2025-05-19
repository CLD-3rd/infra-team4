package com.cloudboot.room_reservation.util.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
	
	private HttpStatus httpStatus;
	private String errorMessage;

	public ApiException(HttpStatus httpStatus, String errorMessage) {
		super(errorMessage);
	}
}
