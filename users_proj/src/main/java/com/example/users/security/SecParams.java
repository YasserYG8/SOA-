package com.example.users.security;

public final class SecParams {

    public static final long EXP_TIME = 24 * 60 * 60 * 1000L; // tokens last 1 day
    public static final String PREFIX = "Bearer ";

    // users_proj signs tokens with this key and cars_proj checks them, so both must use the same value.
    // The fallback is public on GitHub and only suits local development: set JWT_SECRET anywhere else.
    public static final String SECRET = System.getenv().getOrDefault("JWT_SECRET",
            "carhub-local-development-only-secret-key");

    private SecParams() {
    }
}
