package com.backend_challenge.backendChallenge.dtos;

public record BookResponse(

        String title,
        String authorName,
        String ISBN,
        Integer timesRented

) {
}
