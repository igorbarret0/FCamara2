package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.dtos.BookRequest;
import com.backend_challenge.backendChallenge.dtos.BookResponse;
import com.backend_challenge.backendChallenge.dtos.UpdateBookRequest;
import com.backend_challenge.backendChallenge.entites.Book;
import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.mapper.BookMapper;
import com.backend_challenge.backendChallenge.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public void saveBook(BookRequest request, Authentication connectedUser) {

        var user = (User) connectedUser.getPrincipal();

        if (user.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only admins can access this route");
        }

        Book newBook = new Book();
        newBook.setTitle(request.title());
        newBook.setAuthorName(request.authorName());
        newBook.setISBN(request.ISBN());
        newBook.setTimesRented(0);

        bookRepository.save(newBook);
    }

    public List<BookResponse> findAllBooks() {

        return bookRepository.findAll()
                .stream().map(
                        book -> bookMapper.toBookResponse(book)
                ).toList();
    }

    public BookResponse findBookById(Long bookId) {

        return bookRepository.findById(bookId)
                .map(book -> bookMapper.toBookResponse(book)
                ).orElseThrow(() -> new RuntimeException("Could not found a book with ID: " + bookId));
    }

    public void updateBook(Long bookId, UpdateBookRequest request, Authentication connectedUser) {

        var user = (User) connectedUser.getPrincipal();

        if (user.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only admins can access this route");
        }

        var bookFound = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Could not found a book with ID: " + bookId));

        bookFound.setTitle(request.title());
        bookFound.setAuthorName(request.authorName());

        bookRepository.save(bookFound);
    }
}
