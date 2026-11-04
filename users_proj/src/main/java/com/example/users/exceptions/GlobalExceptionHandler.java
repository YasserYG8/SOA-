package com.example.users.exceptions;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Every error reaches Angular as { errorCode, message }.
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, String>> handleRegistration(RegistrationException e) {
        return body(e.getStatus(), e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        return body(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    // Two registrations with the same username or email at the same moment: the database refuses the second.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicate(DataIntegrityViolationException e) {
        return body(HttpStatus.CONFLICT, "ALREADY_EXISTS", "This username or email is already used.");
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<Map<String, String>> handleMail(MailException e) {
        log.error("Could not send the verification email", e);
        return body(HttpStatus.BAD_GATEWAY, "EMAIL_NOT_SENT", "We could not send the verification email. Please try again later.");
    }

    private ResponseEntity<Map<String, String>> body(HttpStatus status, String errorCode, String message) {
        return ResponseEntity.status(status).body(Map.of("errorCode", errorCode, "message", message));
    }
}
