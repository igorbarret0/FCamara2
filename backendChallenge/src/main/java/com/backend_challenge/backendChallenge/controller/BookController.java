package com.backend_challenge.backendChallenge.controller;

import com.backend_challenge.backendChallenge.dtos.BookRequest;
import com.backend_challenge.backendChallenge.dtos.BookResponse;
import com.backend_challenge.backendChallenge.dtos.UpdateBookRequest;
import com.backend_challenge.backendChallenge.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> saveBook(@RequestBody BookRequest request, Authentication connectedUser) {

        bookService.saveBook(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAllBooks() {

        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable(name = "book-id") Long bookId,
            Authentication connectedUser
    ) {

        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @PatchMapping("/{book-id}")
    public ResponseEntity<Void> updateBook(
            @PathVariable(name = "book-id") Long bookId,
            @RequestBody UpdateBookRequest request,
            Authentication connectedUser
    )  {

        bookService.updateBook(bookId, request, connectedUser);
        return ResponseEntity.ok().build();
    }

}
