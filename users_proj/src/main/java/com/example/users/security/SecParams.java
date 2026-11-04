package com.example.users.security;

public final class SecParams {

    public static final long EXP_TIME = 24 * 60 * 60 * 1000L; // tokens last 1 day
    public static final String PREFIX = "Bearer ";

    private SecParams() {
    }
}
