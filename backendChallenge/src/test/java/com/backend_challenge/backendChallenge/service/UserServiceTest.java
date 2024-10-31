package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.dtos.AddressDto;
import com.backend_challenge.backendChallenge.dtos.UserResponse;
import com.backend_challenge.backendChallenge.entites.Role;
import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.exceptionHandler.AdminOnlyAccessException;
import com.backend_challenge.backendChallenge.exceptionHandler.UserNotFoundException;
import com.backend_challenge.backendChallenge.mapper.UserMapper;
import com.backend_challenge.backendChallenge.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    User user = new User();
    UserResponse userResponse;

    @BeforeEach
    void setUp() {

        user.setName("Atena");
        user.setAddress("Olimpo");
        user.setDelays(0);

        userResponse = new UserResponse(
          user.getName(),
          user.getAddress(),
          user.getDelays()
        );
    }

    @Test
    public void findAllUsers_WhenThereIsUsersAndWithAdminUser_ShouldReturnUserResponseList() {

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userMapper.toUserResponse(user))
                .thenReturn(userResponse);

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        var response = userService.findAllUsers(authentication);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(user.getName(), response.getFirst().name());
        assertEquals(user.getAddress(), response.getFirst().address());
        assertEquals(user.getDelays(), response.getFirst().delays());
    }

    @Test
    public void findAllUsers_WithBNotAdminUser_ShouldThrowException() {

        var userRole = new Role("USER");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        assertThrows(AdminOnlyAccessException.class, () -> {
            userService.findAllUsers(authentication);
        }, "Only admins can access this route");
    }

    @Test
    public void findAllUsers_WhenThereIsNoUser_ShouldReturnEmptyList() {

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        var response = userService.findAllUsers(authentication);

        assertEquals(0,response.size());
    }

    @Test
    public void findUserById_WhenIdIsValid() {

        long userId = 1L;

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(userMapper.toUserResponse(user))
                .thenReturn(userResponse);

        var response = userService.findUserById(userId, authentication);

        assertEquals(user.getName(), response.name());
        assertEquals(user.getAddress(), response.address());
        assertEquals(user.getDelays(), response.delays());
    }

    @Test
    public void findUserById_WithNotAdminUser_ShouldThrowException() {

        long userId = 1L;

        var userRole = new Role("USER");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        assertThrows(AdminOnlyAccessException.class, () -> {
            userService.findUserById(userId, authentication);
        }, "Only admins can access this route");
    }

    @Test
    public void findUserById_WithAdminUser_AndUserCouldNotBeFound_ShouldThrowException() {

        long userId = 99L;

        var userRole = new Role("ADMIN");
        user.setRoles(List.of(userRole));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findUserById(userId, authentication);
        }, "Could not found user with the provided ID: " + userId);
    }

    @Test
    public void updateUser_ShouldUpdateUserSuccessfully() {

        var addressDto = new AddressDto("new Address");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal())
                .thenReturn(user);

        userService.updateUser(addressDto, authentication);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        var copyValue = userCaptor.getValue();

        assertEquals(addressDto.address(), copyValue.getAddress());
    }

}
