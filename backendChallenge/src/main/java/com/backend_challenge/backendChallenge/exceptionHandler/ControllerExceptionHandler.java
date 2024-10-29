package com.backend_challenge.backendChallenge.exceptionHandler;

import com.backend_challenge.backendChallenge.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AdminOnlyAccessException.class)
    public ResponseEntity<ExceptionResponse> adminOnlyAccess(AdminOnlyAccessException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "403"
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ExceptionResponse> bookNotFound(BookNotFoundException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "404"
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CopyAlreadyReturnedException.class)
    public ResponseEntity<ExceptionResponse> copyAlreadyReturned(CopyAlreadyReturnedException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "422"
        );

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(CopyNotFoundException.class)
    public ResponseEntity<ExceptionResponse> copyNotFound(CopyNotFoundException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "404"
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTitleException.class)
    public ResponseEntity<ExceptionResponse> invalidTitle(InvalidTitleException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "400"
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAvailableCopiesException.class)
    public ResponseEntity<ExceptionResponse> notAvailableCopies(NotAvailableCopiesException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "409"
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RentalSuspensionException.class)
    public ResponseEntity<ExceptionResponse> rentalSuspension(RentalSuspensionException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "403"
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<ExceptionResponse> roleException(RoleException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "404"
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnrentedBookAccessException.class)
    public ResponseEntity<ExceptionResponse> unrentedBookAccess(UnrentedBookAccessException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "400"
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> userNotFound(UserNotFoundException exception) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                "404"
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
