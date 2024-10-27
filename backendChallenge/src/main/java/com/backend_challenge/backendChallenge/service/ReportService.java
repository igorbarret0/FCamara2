package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.dtos.BookReportDto;
import com.backend_challenge.backendChallenge.entites.Book;
import com.backend_challenge.backendChallenge.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final BookRepository bookRepository;

    public List<BookReportDto> generateMonthlyRentedReports() {

        var response = bookRepository.findTop3BooksMostRentedThisMonth();
        return response.stream()
                .map(resp -> new BookReportDto(
                        resp.getTitle(),
                        resp.getTimesRented()
                )).toList();
    }

    public List<BookReportDto> generateMonthlyDelayedReports() {

        var response = bookRepository.findTop3BooksWithMostDelaysThisMonth();
        return response.stream()
                .map(resp -> new BookReportDto(
                        resp.getTitle(),
                        resp.getTimesDelayed()
                )).toList();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    private void resetTimesRentedAndDelayedAfterOneMonth() {

        List<Book> books = bookRepository.findAll();

        books.forEach(book -> {
            book.setTimesRented(0);
            book.setTimesDelayed(0);
            bookRepository.save(book);
        });
    }




}

