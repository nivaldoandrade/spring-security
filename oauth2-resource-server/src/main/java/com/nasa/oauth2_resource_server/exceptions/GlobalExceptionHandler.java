package com.nasa.oauth2_resource_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        int statusCode = HttpStatus.BAD_REQUEST.value();

        RestErrorResponse error = new RestErrorResponse(
                statusCode,
                "The email or password is incorrect",
                LocalDateTime.now()
        );

        return ResponseEntity.status(statusCode).body(error);
    }

    @ExceptionHandler(RefreshTokenUsedException.class)
    public ResponseEntity<RestErrorResponse> handleRefreshTokenUsedException(RefreshTokenUsedException e) {
        int statusCode = HttpStatus.UNAUTHORIZED.value();

        RestErrorResponse error = new RestErrorResponse(
                statusCode,
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(statusCode).body(error);
    }
}
