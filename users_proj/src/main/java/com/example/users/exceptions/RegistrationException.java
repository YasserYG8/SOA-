package com.example.users.exceptions;

import org.springframework.http.HttpStatus;

// Parent of the registration errors: each one knows its HTTP status and error code.
public abstract class RegistrationException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected RegistrationException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
