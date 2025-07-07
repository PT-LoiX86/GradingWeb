package com.grd.gradingbe.exception;

import com.grd.gradingbe.enums.TokenType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(TokenType tokenType, String validationType, String message) {
        super(String.format("Failed to perform %s when authenticate %s token: '%s'", validationType, tokenType.toString(), message));
    }
}
