package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.dtos.CopyRequest;
import com.backend_challenge.backendChallenge.dtos.CopyResponse;
import com.backend_challenge.backendChallenge.entites.Copy;
import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.mapper.CopyMapper;
import com.backend_challenge.backendChallenge.repository.BookRepository;
import com.backend_challenge.backendChallenge.repository.CopyRepository;
import com.backend_challenge.backendChallenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CopyService {

    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CopyMapper copyMapper;

    public void generateCopy( CopyRequest request, Authentication connectedUser ) {

        var user = (User) connectedUser.getPrincipal();

        if (user.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only admins can access this route");
        }

        var bookToCopy = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new RuntimeException("Could not found a book with the provided ID: " + request.bookId()));

        Copy copy = new Copy();

        if (!bookToCopy.getTitle().equals(request.title())) {
            throw new RuntimeException("The Title of the book you wanna generate a copy is wrong");
        }

        copy.setTitle(request.title());
        copy.setCopyCode(generateCopyCode());
        copy.setAvailable(true);

        copy.setBooks(List.of(bookToCopy));
        bookToCopy.getBookCopies().add(copy);

        copyRepository.save(copy);
        bookRepository.save(bookToCopy);
    }

    public List<CopyResponse> getAllCopies() {

        return copyRepository.findAll()
                .stream().map(
                        copy -> copyMapper.toCopyResponse(copy)
                ).toList();
    }

    public CopyResponse getCopyById(Long copyId) {

        return copyRepository.findById(copyId)
                .map(copy -> copyMapper.toCopyResponse(copy))
                .orElseThrow(() -> new RuntimeException("Could not found a book with the provided ID: " + copyId));
    }

    public String rentCopyBook(Long bookId, Authentication connectedUser) {

        var bookFound = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Could not found book with the provided ID: " + bookId));

        var availableCopy = bookFound.getBookCopies()
                .stream().filter(Copy::isAvailable)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available copies to rent for the book:"  + bookFound.getTitle()));

        var user = (User) connectedUser.getPrincipal();

        if (user.getDelays() > 2) {
            throw new RuntimeException("You delayed you return more than two times, so you cannot rent anymore :(");
        }

        availableCopy.setUser(user);
        availableCopy.setRentedAt(LocalDate.now());
        availableCopy.setAvailable(false);

        copyRepository.save(availableCopy);

        bookFound.setTimesRented(bookFound.getTimesRented() + 1);
        bookRepository.save(bookFound);

        return availableCopy.getCopyCode();
    }

    public void returnRentedBook( String copyCode, Authentication connectedUser) {

        var copy = copyRepository.findCopyByCopyCode(copyCode)
                .orElseThrow(() -> new RuntimeException("No copy found with the provided Code: " + copyCode));

        var book = bookRepository.findBookByTitle(copy.getTitle())
                .orElseThrow(() -> new RuntimeException("Book not found with the provided Title: " + copy.getTitle()));

        if (copy.isAvailable()) {
            throw new RuntimeException("This copy is already returned.");
        }

        var user = (User) connectedUser.getPrincipal();

        if (!copy.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You not rented this book");
        }

        LocalDate dueDate = copy.getRentedAt().plusWeeks(1);
        boolean isLate = LocalDate.now().isAfter(dueDate);

        copy.setRentedAt(null);
        copy.setUser(null);
        copy.setAvailable(true);

        if (isLate) {
            user.setDelays(user.getDelays() + 1);
            book.setTimesDelayed(book.getTimesDelayed() + 1);
        }

        copyRepository.save(copy);
        userRepository.save(user);

    }

    private String generateCopyCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder copyCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            int index = random.nextInt(characters.length());
            copyCode.append(characters.charAt(index));
        }

        return copyCode.toString();
    }

}
