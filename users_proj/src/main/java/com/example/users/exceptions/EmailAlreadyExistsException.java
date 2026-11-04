package com.example.users.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends RegistrationException {
    public EmailAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "An account already uses this email.");
    }
}
