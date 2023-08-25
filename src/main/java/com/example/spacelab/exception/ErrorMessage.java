package com.example.spacelab.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class ErrorMessage {

    private String name;
    private Instant timestamp;
    private Integer statusCode;
    private Map<String, String> errors;

    public ErrorMessage(String name, Integer statusCode, Map<String, String> errors) {
        this.name = name;
        this.statusCode = statusCode;
        this.timestamp = Instant.now();
        this.errors = errors;
    }

}
