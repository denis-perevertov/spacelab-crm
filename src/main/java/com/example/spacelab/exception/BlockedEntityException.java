package com.example.spacelab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class BlockedEntityException extends RuntimeException {
    public BlockedEntityException() {
        super("entity.blocked");
    }
}
