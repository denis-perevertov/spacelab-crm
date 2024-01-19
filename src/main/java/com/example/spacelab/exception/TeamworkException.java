package com.example.spacelab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TeamworkException extends RuntimeException {

    public TeamworkException(List<Object> errors) {

    }
}
