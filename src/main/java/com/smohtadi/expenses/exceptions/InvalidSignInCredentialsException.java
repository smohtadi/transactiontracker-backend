package com.smohtadi.expenses.exceptions;

public class InvalidSignInCredentialsException extends RuntimeException {
    public InvalidSignInCredentialsException() {
        super("Invalid credentials provided.");
    }
}
