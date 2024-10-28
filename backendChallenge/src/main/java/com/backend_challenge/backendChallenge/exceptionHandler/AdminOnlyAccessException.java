package com.backend_challenge.backendChallenge.exceptionHandler;

public class AdminOnlyAccessException extends RuntimeException {

    public AdminOnlyAccessException(String message) {
        super(message);
    }

}
