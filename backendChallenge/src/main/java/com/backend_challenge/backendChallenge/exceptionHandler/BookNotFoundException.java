package com.backend_challenge.backendChallenge.exceptionHandler;

import org.hibernate.annotations.NotFound;

public class BookNotFoundException extends RuntimeException {

        public BookNotFoundException(String message) {
            super(message);
        }

}
