package com.example.cars.security;

public final class SecParams {

    public static final String PREFIX = "Bearer ";

    // Must match the key users_proj signs tokens with.
    // The fallback is public on GitHub and only suits local development: set JWT_SECRET anywhere else.
    public static final String SECRET = System.getenv().getOrDefault("JWT_SECRET",
            "carhub-local-development-only-secret-key");

    private SecParams() {
    }
}
