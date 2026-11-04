package com.example.users.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RegistrationException {
    public InvalidTokenException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "This code is not valid. Check it and try again.");
    }
}
