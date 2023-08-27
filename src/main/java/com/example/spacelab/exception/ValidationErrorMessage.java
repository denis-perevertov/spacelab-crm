package com.example.spacelab.exception;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorMessage(String errorName,
                                     Instant timestamp,
                                     Integer statusCode,
                                     Map<String, String> errors) {

    public ValidationErrorMessage(Integer statusCode, Map<String, String> errors) {
        this("Validation error", Instant.now(), statusCode, errors);
    }
}
