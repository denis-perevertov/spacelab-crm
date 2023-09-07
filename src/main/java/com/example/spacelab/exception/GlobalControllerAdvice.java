package com.example.spacelab.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
@Log
public class GlobalControllerAdvice {


    @ExceptionHandler(ObjectValidationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> validationFailedHandler(ObjectValidationException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Validation error", HttpStatus.BAD_REQUEST.value(), ex.getErrors()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> resourceNotFoundHandler(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Resource not found", HttpStatus.NOT_FOUND.value(), Map.of(ex.getResourceClassName(), ex.getMessage())),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> expiredTokenHandler(ExpiredJwtException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Token expired", HttpStatus.UNAUTHORIZED.value(), Map.of("token", ex.getMessage())),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> tokenExceptionHandler(TokenException e) {
        return new ResponseEntity<>(
                new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST.value(), Map.of("token", e.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> fallbackInternalErrorHandler(Exception ex) {
        log.severe(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>("Unknown server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
