package com.backend_challenge.backendChallenge.exceptionHandler;

public class CopyAlreadyReturnedException extends RuntimeException {

    public CopyAlreadyReturnedException(String message) {
        super(message);
    }
}
