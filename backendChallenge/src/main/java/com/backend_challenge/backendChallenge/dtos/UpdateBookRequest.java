package com.backend_challenge.backendChallenge.dtos;

public record UpdateBookRequest(
        String title,
        String authorName
) {
}
