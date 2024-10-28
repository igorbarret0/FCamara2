package com.backend_challenge.backendChallenge.exceptionHandler;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
