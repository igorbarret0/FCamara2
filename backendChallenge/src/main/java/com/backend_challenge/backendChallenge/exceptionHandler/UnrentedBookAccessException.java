package com.backend_challenge.backendChallenge.exceptionHandler;

public class UnrentedBookAccessException extends RuntimeException {

    public UnrentedBookAccessException(String message) {
        super(message);
    }
}
