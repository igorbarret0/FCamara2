package com.backend_challenge.backendChallenge.exception;

import java.util.Date;

public record ExceptionResponse(

        Date timeStamp,
        String message,
        String statusCode
) {
}
