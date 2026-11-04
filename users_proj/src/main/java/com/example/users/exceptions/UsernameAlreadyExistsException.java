package com.example.users.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends RegistrationException {
    public UsernameAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS", "This username is already taken.");
    }
}
