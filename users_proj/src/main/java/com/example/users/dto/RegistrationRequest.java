package com.example.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Enter a username of at least 3 characters.")
        @Size(min = 3, message = "Enter a username of at least 3 characters.")
        String username,

        @NotBlank(message = "Enter a valid email address.")
        @Email(message = "Enter a valid email address.")
        String email,

        @NotBlank(message = "Use a password of at least 6 characters.")
        @Size(min = 6, message = "Use a password of at least 6 characters.")
        String password) {
}
