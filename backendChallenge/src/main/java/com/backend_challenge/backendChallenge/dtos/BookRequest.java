package com.backend_challenge.backendChallenge.dtos;

public record BookRequest(

        String title,
        String authorName,
        String ISBN
) {
}
