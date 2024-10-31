package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.entites.Role;
import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.exceptionHandler.RoleException;
import com.backend_challenge.backendChallenge.repository.RoleRepository;
import com.backend_challenge.backendChallenge.repository.UserRepository;
import com.backend_challenge.backendChallenge.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import request.LoginRequest;
import request.LoginResponse;
import request.RegisterRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthenticationService authenticationService;

    RegisterRequest registerRequest;

    LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {

        LocalDate birthDate = LocalDate.of(1999, 7, 23);
        registerRequest = new RegisterRequest(
                "Peter Park",
                "12345678",
                "004-000-091-00",
                birthDate,
                "Some place"
        );

        loginRequest = new LoginRequest(
                "Lionel Messi",
                "12345678"
        );

    }

    @Test
    public void givenAnRegisterRequest_ShouldSaveTheObjectSuccessfully() {

        Role userRole = new Role("USER");

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(userRole));

        when(passwordEncoder.encode(registerRequest.password()))
                .thenReturn("encodedPassword");

        authenticationService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("Peter Park", savedUser.getName());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("004-000-091-00", savedUser.getCpf());
        assertEquals(registerRequest.birthDate(), savedUser.getBirthDate());
        assertEquals("Some place", savedUser.getAddress());
        assertTrue(savedUser.getRoles().contains(userRole));
    }

    @Test
    public void givenRoleNotFound_WhenRegister_ThenThrowRoleException() {

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.empty());

        assertThrows(RoleException.class, () -> authenticationService.register(registerRequest),
                "Role USER was not initialized");
    }

    @Test
    public void givenAnValidLoginRequest_ShouldReturnAnLoginResponse() {

        User user = new User();
        user.setName("Lionel Messi");

        String tokenToReturn = "janbASD1239ASD_KSJDN";
        Authentication authentication = mock(Authentication.class);

       when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
               .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(jwtService.generateToken(any(HashMap.class), eq(user)))
                .thenReturn(tokenToReturn);

        LoginResponse response = authenticationService.login(loginRequest);

        assertNotNull(response);
        assertEquals(tokenToReturn, response.token());
    }

}
