package com.backend_challenge.backendChallenge.repository;

import com.backend_challenge.backendChallenge.entites.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByTitle(String title);

    @Query(value = "SELECT b FROM Book b ORDER BY b.timesRented DESC LIMIT 3")
    List<Book> findTop3BooksMostRentedThisMonth();

    @Query(value = "SELECT b FROM Book b ORDER BY b.timesDelayed DESC LIMIT 3")
    List<Book> findTop3BooksWithMostDelaysThisMonth();





}
