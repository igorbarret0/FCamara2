package com.backend_challenge.backendChallenge.mapper;

import com.backend_challenge.backendChallenge.dtos.BookResponse;
import com.backend_challenge.backendChallenge.entites.Book;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public BookResponse toBookResponse(Book book) {

        BookResponse bookResponse = new BookResponse(
                book.getTitle(),
                book.getAuthorName(),
                book.getISBN(),
                book.getTimesRented()
        );

        return bookResponse;
    }

}
