package com.grd.gradingbe.exception;

import com.grd.gradingbe.dto.response.ErrorResponse;
import com.grd.gradingbe.dto.response.ValidationErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.*;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields",
                validationErrors,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception, WebRequest webRequest) {
        exception.printStackTrace();

        ErrorResponse errorResponse;
        HttpStatus status;

        if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = new ErrorResponse(
                    webRequest.getDescription(false),
                    status,
                    "The username or password is incorrect",
                    LocalDateTime.now()
            );
        } else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse(
                    webRequest.getDescription(false),
                    status,
                    "The account is locked",
                    LocalDateTime.now()
            );
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse(
                    webRequest.getDescription(false),
                    status,
                    "You are not authorized to access this resource",
                    LocalDateTime.now()
            );
        } else if (exception instanceof SignatureException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = new ErrorResponse(
                    webRequest.getDescription(false),
                    status,
                    "The JWT signature is invalid",
                    LocalDateTime.now()
            );
        } else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = new ErrorResponse(
                    webRequest.getDescription(false),
                    status,
                    "The JWT token has expired",
                    LocalDateTime.now()
            );
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = new ErrorResponse(
                    webRequest.getDescription(false),
                    status,
                    "Unknown internal server error.",
                    LocalDateTime.now()
            );
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorResponse errorResponseDTO = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleLoanAlreadyExistsException(ResourceAlreadyExistException exception, WebRequest webRequest) {
        ErrorResponse errorResponseDTO = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
