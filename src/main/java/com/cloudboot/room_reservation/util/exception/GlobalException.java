package com.cloudboot.room_reservation.util.exception;

import com.cloudboot.room_reservation.member.exception.PasswordMismatchException;
import com.cloudboot.room_reservation.member.exception.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResult> handleUsernameExists(UsernameAlreadyExistsException e) {
        ErrorResult errorResult = new ErrorResult("UsernameExists", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResult);  // 409 Conflict
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResult> handlePasswordMismatch(PasswordMismatchException e) {
        ErrorResult errorResult = new ErrorResult("PasswordMismatch", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult); // 400 Bad Request
    }

    // 다른 RuntimeException은 일반 400으로 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResult> handleRuntimeException(RuntimeException e) {
        ErrorResult errorResult = new ErrorResult("RuntimeException", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorResults = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorResults.add(error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResults);
    }

}