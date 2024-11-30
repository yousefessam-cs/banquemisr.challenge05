package com.banquemisr.challenge05.exception;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }
    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}