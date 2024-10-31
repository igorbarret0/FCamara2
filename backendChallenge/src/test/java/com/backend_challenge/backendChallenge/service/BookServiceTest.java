package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.dtos.BookRequest;
import com.backend_challenge.backendChallenge.dtos.BookResponse;
import com.backend_challenge.backendChallenge.dtos.UpdateBookRequest;
import com.backend_challenge.backendChallenge.entites.Book;
import com.backend_challenge.backendChallenge.entites.Role;
import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.exceptionHandler.AdminOnlyAccessException;
import com.backend_challenge.backendChallenge.exceptionHandler.BookNotFoundException;
import com.backend_challenge.backendChallenge.mapper.BookMapper;
import com.backend_challenge.backendChallenge.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookService bookService;

    BookRequest bookRequest;

    BookResponse bookResponse;

    UpdateBookRequest updateBookRequest;

    Book bookEntity = new Book();

    User user = new User();


    @BeforeEach
    void setUp() {

        bookEntity.setTitle("Percy Jackson");
        bookEntity.setAuthorName("Rick Riordan");
        bookEntity.setISBN("98754332");
        bookEntity.setTimesRented(0);

        bookRequest = new BookRequest(
                "Percy Jackson",
                "Rick Riordan",
                "98754332"
        );

        bookResponse = new BookResponse(
                "Percy Jackson",
                "Rick Riordan",
                "98754332",
                0
        );

        updateBookRequest = new UpdateBookRequest(
                "Percy Jackson e os Olimpianos",
                "Rick Riordan Updated"
        );

        user.setName("Rick Riordan");
    }

    @Test
    public void givenAnBookRequestWithAdminUser_ShouldSaveBookObjectSuccessfully() {

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(user);

        bookService.saveBook(bookRequest, authentication);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());

        Book savedBook = bookCaptor.getValue();

        assertEquals(bookRequest.title(), savedBook.getTitle());
        assertEquals(bookRequest.authorName(), savedBook.getAuthorName());
        assertEquals(bookRequest.ISBN(), savedBook.getISBN());
        assertEquals(0, savedBook.getTimesRented());
    }

    @Test
    public void givenAnBookRequestWithNoAdminUser_ShouldThrowException() {

        var userRole = new Role("USER");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(user);

        assertThrows(AdminOnlyAccessException.class, () -> {
            bookService.saveBook(bookRequest, authentication);
        }, "Only Admins can access this route");
    }

    @Test
    public void findAllBooks_ShouldReturnAnListOfBooks_WhenThereIsBook() {

        when(bookRepository.findAll())
                .thenReturn(List.of(bookEntity));

        when(bookMapper.toBookResponse(any(Book.class)))
                .thenReturn(bookResponse);

        var response = bookService.findAllBooks();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(bookEntity.getTitle(), response.getFirst().title());
        assertEquals(bookEntity.getAuthorName(), response.getFirst().authorName());
        assertEquals(bookEntity.getISBN(), response.getFirst().ISBN());
        assertEquals(bookEntity.getTimesRented(), response.getFirst().timesRented());
    }

    @Test
    public void findAllBooks_ShouldReturnEmptyList_WhenThereIsNoBook() {

        when(bookRepository.findAll())
                .thenReturn(Collections.emptyList());

        var response = bookService.findAllBooks();

        assertEquals(0, response.size());

    }

    @Test
    public void findBookById_ShouldReturnBookObject_WhenIdIsValid() {

        Long id = 1L;

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookEntity));

        when(bookMapper.toBookResponse(bookEntity))
                .thenReturn(bookResponse);

        var response = bookService.findBookById(id);

        assertNotNull(response);
        assertEquals(bookEntity.getTitle(), response.title());
        assertEquals(bookEntity.getAuthorName(), response.authorName());
        assertEquals(bookEntity.getISBN(), response.ISBN());
        assertEquals(bookEntity.getTimesRented(), response.timesRented());
    }

    @Test
    public void findBookById_ShouldThrowException_WhenIdIsInvalid() {

        Long id = 99L;

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.findBookById(id);
        }, "Could not found a book with ID: " + id);

    }

    @Test
    public void updateBook_WithAdminUser_AndBookIdValid() {

        Long bookId = 1L;

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookEntity));


        bookService.updateBook(bookId, updateBookRequest, authentication);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());

        var bookValue = bookCaptor.getValue();

        assertEquals(updateBookRequest.title(), bookValue.getTitle());
        assertEquals(updateBookRequest.authorName(), bookValue.getAuthorName());
    }

    @Test
    public void updateBook_WithNoAdminUser_AndBookIdValid_ShouldThrowException() {

        Long bookId = 1L;

        var userRole = new Role("USER");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        assertThrows(AdminOnlyAccessException.class, () -> {
            bookService.updateBook(bookId, updateBookRequest, authentication);
        }, "Only Admins can access this route");
    }

    @Test
    public void updateBook_WithAdminUser_AndBookIdInvalid_ShouldThrowException() {

        Long bookId = 99L;

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.empty());


        assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(bookId, updateBookRequest, authentication);
        }, "Could not found a book with ID: " + bookId);
    }

}
