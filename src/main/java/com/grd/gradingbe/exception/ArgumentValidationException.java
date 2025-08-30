package com.grd.gradingbe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArgumentValidationException extends RuntimeException {
    public ArgumentValidationException(String message) {
        super(message);
    }
}
