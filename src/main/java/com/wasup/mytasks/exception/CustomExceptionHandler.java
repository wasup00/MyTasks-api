package com.wasup.mytasks.exception;

import com.wasup.mytasks.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse().status(HttpStatus.NOT_FOUND.value()).message(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(Exception e) {
        ErrorResponse errorResponse =
                new ErrorResponse().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception e) {
        ErrorResponse errorResponse =
                new ErrorResponse().status(HttpStatus.BAD_REQUEST.value()).message(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(Exception e) {
        ErrorResponse errorResponse =
                new ErrorResponse().status(HttpStatus.BAD_REQUEST.value()).message(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse =
                new ErrorResponse().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
