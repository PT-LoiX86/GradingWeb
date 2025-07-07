package com.grd.gradingbe.exception;

import com.grd.gradingbe.enums.TokenTypes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtManagementException extends RuntimeException {
    public JwtManagementException(TokenTypes tokenType, String activity, String message) {
        super(String.format("Failed to perform %s %s token: '%s'", activity, tokenType.toString(), message));
    }
}

