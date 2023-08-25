package com.example.spacelab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectValidationException extends RuntimeException {
    Map<String, String> errors;

    public ObjectValidationException(Map<String, String> errors) {
        super("Validation error");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {return this.errors;}
}
