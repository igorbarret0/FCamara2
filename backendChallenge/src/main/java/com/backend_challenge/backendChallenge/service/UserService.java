package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.dtos.AddressDto;
import com.backend_challenge.backendChallenge.dtos.UserResponse;
import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.mapper.UserMapper;
import com.backend_challenge.backendChallenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> findAllUsers(Authentication connectedUser) {

        var user = (User) connectedUser.getPrincipal();

        if (user.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only admins can access this route");
        }

        return userRepository.findAll()
                .stream().map(
                        u -> userMapper.toUserResponse(u)
                ).toList();
    }

    public UserResponse findUserById(Long userId, Authentication connectedUser) {

        var user = (User) connectedUser.getPrincipal();

        if (user.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only admins can access this route");
        }

        return userRepository.findById(userId)
                .map(
                        u -> userMapper.toUserResponse(u)
                ).orElseThrow(() -> new RuntimeException("Could not found user with the provided ID: " + userId));
    }

    public void updateUser(AddressDto address, Authentication connectedUser) {

        var user = (User) connectedUser.getPrincipal();
        user.setAddress(address.address());

        userRepository.save(user);
    }

}
