package com.example.spacelab.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class ErrorMessage {

    @Schema(example = "ErrorName")
    private String name;
    private Instant timestamp;
    @Schema(example = "400")
    private Integer statusCode;
    private Map<String, String> errors;

    public ErrorMessage(String name, Integer statusCode, Map<String, String> errors) {
        this.name = name;
        this.statusCode = statusCode;
        this.timestamp = Instant.now();
        this.errors = errors;
    }

}
