package com.backend_challenge.backendChallenge.controller;

import com.backend_challenge.backendChallenge.dtos.AddressDto;
import com.backend_challenge.backendChallenge.dtos.UserResponse;
import com.backend_challenge.backendChallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers(
            Authentication connectedUser
    ) {

        return ResponseEntity.ok(userService.findAllUsers(connectedUser));
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> findUserById(
            @PathVariable(name = "user-id") Long userId,
            Authentication connectedUser) {

        return ResponseEntity.ok(userService.findUserById(userId, connectedUser));
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(
            @RequestBody AddressDto address,
            Authentication connectedUser) {

        userService.updateUser(address, connectedUser);
        return ResponseEntity.ok().build();
    }

}
