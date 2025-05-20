package com.cloudboot.room_reservation.util.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
	
	private final HttpStatus httpStatus;

	public ApiException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}
}
