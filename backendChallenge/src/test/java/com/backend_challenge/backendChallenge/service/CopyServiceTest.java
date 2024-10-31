package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.dtos.CopyRequest;
import com.backend_challenge.backendChallenge.dtos.CopyResponse;
import com.backend_challenge.backendChallenge.entites.Book;
import com.backend_challenge.backendChallenge.entites.Copy;
import com.backend_challenge.backendChallenge.entites.Role;
import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.exceptionHandler.*;
import com.backend_challenge.backendChallenge.mapper.CopyMapper;
import com.backend_challenge.backendChallenge.repository.BookRepository;
import com.backend_challenge.backendChallenge.repository.CopyRepository;
import com.backend_challenge.backendChallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CopyServiceTest {

    @Mock
    CopyRepository copyRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CopyMapper copyMapper;

    @InjectMocks
    CopyService copyService;

    User user = new User();

    CopyRequest copyRequest;

    Copy copyEntity = new Copy();

    CopyResponse copyResponse;

    Book bookEntity = new Book();

    @BeforeEach
    void setUp() {

        user.setName("Username");
;
        bookEntity.setTitle("Percy Jackson");
        bookEntity.setAuthorName("Rick Riordan");
        bookEntity.setISBN("98754332");
        bookEntity.setTimesRented(0);
        bookEntity.setBookCopies(new ArrayList<>());

        copyEntity.setId(1L);
        copyEntity.setTitle("Percy Jackson");
        copyEntity.setAvailable(true);

        copyResponse = new CopyResponse(
                1L,
                "Percy Jackson",
                true
        );

        copyRequest = new CopyRequest(
                1L,
                "Percy Jackson"
        );
    }

    @Test
    public void generateCopyWithAdminUser_AndValidCopyRequest() {

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookEntity));

        copyService.generateCopy(copyRequest, authentication);

        ArgumentCaptor<Copy> copyCaptor = ArgumentCaptor.forClass(Copy.class);
        verify(copyRepository).save(copyCaptor.capture());

        Copy capturedCopy = copyCaptor.getValue();
        assertNotNull(capturedCopy);
        assertEquals(bookEntity.getTitle(), capturedCopy.getTitle());
        assertTrue(capturedCopy.isAvailable());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());

        Book capturedBook = bookCaptor.getValue();
        assertNotNull(capturedBook.getBookCopies());
        assertTrue(capturedBook.getBookCopies().contains(capturedCopy));
    }

    @Test
    public void generateCopyWithNoAdminUser_AndValidCopyRequest() {

        Role userRole = new Role("USER");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(user);

        assertThrows(AdminOnlyAccessException.class, () -> {
            copyService.generateCopy(copyRequest, authentication);
        }, "Only admins can access this route");
    }

    @Test
    public void generateCopyWithAdminUser_AndInvalidBookId() {

        Role userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            copyService.generateCopy(copyRequest, authentication);
        }, "Could not found a book with the provided ID: " + copyRequest.bookId());

    }

    @Test
    public void generateCopyWithAdminUser_AndInvalidTitle() {

        Book invalidBook = new Book();
        invalidBook.setTitle("Percy");

        Role userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(invalidBook));

        assertThrows(InvalidTitleException.class, () -> {
            copyService.generateCopy(copyRequest, authentication);
        }, "The Title of the book you wanna generate a copy is wrong");

    }

    @Test
    public void getAllCopies_WhenThereIsCopy_ShouldReturnAnCopyResponseList() {

        var bookList = List.of(copyEntity);

        when(copyRepository.findAll())
                .thenReturn(bookList);

        when(copyMapper.toCopyResponse(copyEntity))
                .thenReturn(copyResponse);

        var response = copyService.getAllCopies();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(copyResponse.id(), response.getFirst().id());
        assertEquals(copyResponse.title(), response.getFirst().title());
        assertEquals(copyResponse.isAvailable(), response.getFirst().isAvailable());

    }

    @Test
    public void getAllCopies_WhenThereIsNoCopy_ShouldReturnAnEmptyList() {

        when(copyRepository.findAll())
                .thenReturn(Collections.emptyList());

        var response = copyService.getAllCopies();

        assertEquals(0, response.size());
    }

    @Test
    public void getCopyById_WhenIdIsValid_ShouldReturnCopyResponse() {

        when(copyRepository.findById(anyLong()))
                .thenReturn(Optional.of(copyEntity));

        when(copyMapper.toCopyResponse(copyEntity))
                .thenReturn(copyResponse);

        var response = copyService.getCopyById(1L);

        assertNotNull(response);
        assertEquals(copyEntity.getId(), response.id());
        assertEquals(copyEntity.getTitle(), response.title());
        assertEquals(copyEntity.isAvailable(), response.isAvailable());
    }

    @Test
    public void getCopyById_WhenIdIsInvalid_ShouldThrowException() {

        long invalidBookId =99L;

        when(copyRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(CopyNotFoundException.class, () -> {
            copyService.getCopyById(invalidBookId);
        }, "Could not found a book with the provided ID: " + invalidBookId);
    }

    @Test
    public void rentCopyBook_WhenThereIsAvailableCopyToRent() {

        long bookId = 1L;

        copyEntity.setCopyCode("SOME COPY CODE");

        bookEntity.setBookCopies(List.of(copyEntity));
        bookEntity.setId(1L);
        bookEntity.setTimesRented(0);

        Role userRole = new Role("USER");
        user.setRoles(List.of(userRole));
        user.setDelays(0);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookEntity));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        var response = copyService.rentCopyBook(bookId, authentication);

        ArgumentCaptor<Copy> copyCaptor = ArgumentCaptor.forClass(Copy.class);
        verify(copyRepository).save(copyCaptor.capture());
        var copyValue = copyCaptor.getValue();

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        var bookValue = bookCaptor.getValue();

        assertNotNull(response);
        assertEquals(copyEntity.getId(), copyValue.getId());
        assertEquals(copyEntity.getTitle(), copyValue.getTitle());
        assertEquals(copyEntity.getRentedAt(), copyValue.getRentedAt());

        assertEquals(bookEntity.getTimesRented(), bookValue.getTimesRented());
    }

    @Test
    public void rentCopyBook_WhenBookCouldNotBeFound_ShouldThrowException() {

        long invalidBookId = 99L;

        Authentication authentication = mock(Authentication.class);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            copyService.rentCopyBook(invalidBookId, authentication);
        }, "Could not found book with the provided ID: " + invalidBookId);
    }

    @Test
    public void rentCopyBook_WhenThereIsNoAvailableCopiesForTheBook() {

        copyEntity.setAvailable(false);
        bookEntity.setBookCopies(List.of(copyEntity));

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookEntity));

        Authentication authentication = mock(Authentication.class);

        assertThrows(NotAvailableCopiesException.class, () -> {
            copyService.rentCopyBook(1L, authentication);
        }, "No available copies to rent for the book:" + bookEntity.getTitle());

    }

    @Test
    public void rentCopyBook_WhenUserIsNotAllowed_BecauseDelays() {

        long bookId = 1L;
        user.setDelays(3);

        bookEntity.setBookCopies(List.of(copyEntity));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookEntity));

        assertThrows(RentalSuspensionException.class, () -> {
            copyService.rentCopyBook(bookId, authentication);
        }, "ou delayed you return more than two times, so you cannot rent anymore :(");
    }

    @Test
    public void returnRentedBook_ShouldDoesNotThrowException_AndTheReturnIsNotLate() {

        String copyCode = "09233483534";

        user.setId(1L);
        copyEntity.setUser(user);
        copyEntity.setAvailable(false);
        copyEntity.setRentedAt(LocalDate.now());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(copyRepository.findCopyByCopyCode(anyString()))
                .thenReturn(Optional.of(copyEntity));

        when(bookRepository.findBookByTitle(anyString()))
                .thenReturn(Optional.of(bookEntity));

        copyService.returnRentedBook(copyCode, authentication);

        ArgumentCaptor<Copy> copyCaptor = ArgumentCaptor.forClass(Copy.class);
        verify(copyRepository).save(copyCaptor.capture());

        var copyValue = copyCaptor.getValue();

        assertNull(copyValue.getRentedAt());
        assertNull(copyValue.getUser());
        assertTrue(copyValue.isAvailable());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void returnRentedBook_ShouldDoesNotThrowException_AndTheReturnIsLate() {

        String copyCode = "09233483534";

        user.setId(1L);
        user.setDelays(0);

        bookEntity.setTimesDelayed(0);

        copyEntity.setUser(user);
        copyEntity.setAvailable(false);
        copyEntity.setRentedAt(LocalDate.now().minusWeeks(2));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(copyRepository.findCopyByCopyCode(anyString()))
                .thenReturn(Optional.of(copyEntity));

        when(bookRepository.findBookByTitle(anyString()))
                .thenReturn(Optional.of(bookEntity));

        assertDoesNotThrow(() -> copyService.returnRentedBook(copyCode, authentication));

        ArgumentCaptor<Copy> copyCaptor = ArgumentCaptor.forClass(Copy.class);
        verify(copyRepository).save(copyCaptor.capture());
        var copyValue = copyCaptor.getValue();

        assertNull(copyValue.getRentedAt());
        assertNull(copyValue.getUser());
        assertTrue(copyValue.isAvailable());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        var userValue = userCaptor.getValue();

        assertEquals(1, userValue.getDelays());

        assertEquals(1, bookEntity.getTimesDelayed());

        verify(copyRepository, times(1)).save(any(Copy.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void returnRentedBook_WhenCopyCouldNotBeFound() {

        String copyCode = "29834293";

        Authentication authentication = mock(Authentication.class);

        when(copyRepository.findCopyByCopyCode(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(CopyNotFoundException.class, () -> {
            copyService.returnRentedBook(copyCode, authentication);
        }, "No copy found with the provided Code: " + copyCode);
    }

    @Test
    public void returnRentedBook_WhenBookCouldNotBeFound() {

        String copyCode = "29834293";

        Authentication authentication = mock(Authentication.class);

        when(copyRepository.findCopyByCopyCode(anyString()))
                .thenReturn(Optional.of(copyEntity));

        when(bookRepository.findBookByTitle(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            copyService.returnRentedBook(copyCode, authentication);
        }, "Book not found with the provided Title: " + copyEntity.getTitle());
    }

    @Test
    public void returnRentedBook_WhenCopyIsAlreadyReturned() {

        String copyCode = "29834293";
        copyEntity.setAvailable(true);

        Authentication authentication = mock(Authentication.class);

        when(copyRepository.findCopyByCopyCode(anyString()))
                .thenReturn(Optional.of(copyEntity));

        when(bookRepository.findBookByTitle(anyString()))
                .thenReturn(Optional.of(bookEntity));

        assertThrows(CopyAlreadyReturnedException.class, () -> {
            copyService.returnRentedBook(copyCode, authentication);
        }, "This copy is already returned.");
    }

    @Test
    public void returnRentedBook_WhenTheUserNotRentedTheBook() {

        var newUser = new User();
        newUser.setId(2L);

        user.setId(1L);

        String copyCode = "29834293";
        copyEntity.setAvailable(false);
        copyEntity.setUser(newUser);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(copyRepository.findCopyByCopyCode(anyString()))
                .thenReturn(Optional.of(copyEntity));

        when(bookRepository.findBookByTitle(anyString()))
                .thenReturn(Optional.of(bookEntity));

        assertThrows(UnrentedBookAccessException.class, () -> {
            copyService.returnRentedBook(copyCode, authentication);
        }, "You not rented this book");
    }

}
