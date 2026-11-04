package com.example.users.exceptions;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends RegistrationException {
    public ExpiredTokenException() {
        super(HttpStatus.BAD_REQUEST, "EXPIRED_TOKEN", "This code has expired. Register again to get a new one.");
    }
}
