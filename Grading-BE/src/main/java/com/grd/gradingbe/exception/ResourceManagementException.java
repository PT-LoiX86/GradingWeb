package com.grd.gradingbe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceManagementException extends RuntimeException
{
    public ResourceManagementException(String method, String object, String message) {
        super(String.format("Failed to perform %s with object %s: '%s'", method, object, message));
    }
}

