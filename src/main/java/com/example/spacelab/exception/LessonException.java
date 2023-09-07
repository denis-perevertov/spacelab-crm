package com.example.spacelab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LessonException extends RuntimeException{

    public LessonException() {
        super();
    }

    public LessonException(String message) {
        super(message);
    }
}
