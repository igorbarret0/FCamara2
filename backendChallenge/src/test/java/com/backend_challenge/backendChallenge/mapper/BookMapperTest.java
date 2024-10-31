package com.backend_challenge.backendChallenge.mapper;

import com.backend_challenge.backendChallenge.dtos.BookResponse;
import com.backend_challenge.backendChallenge.entites.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookMapperTest {

    @Test
    public void toBookResponseTest() {

        Book book = new Book();
        book.setTitle("The Great Gatsby");
        book.setAuthorName("F. Scott");
        book.setISBN("T1323453");
        book.setTimesRented(5);

        BookMapper bookMapper = new BookMapper();
        BookResponse bookResponse = bookMapper.toBookResponse(book);

        assertNotNull(bookResponse, "The object returned should not be null");
        assertTrue(bookResponse instanceof BookResponse, "The object returned should be a instance of BookResponse");

        assertEquals("The Great Gatsby", bookResponse.title());
        assertEquals("F. Scott", bookResponse.authorName());
        assertEquals("T1323453", bookResponse.ISBN());
        assertEquals(5, bookResponse.timesRented());
    }

}
