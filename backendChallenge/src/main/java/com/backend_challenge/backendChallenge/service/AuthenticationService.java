package com.backend_challenge.backendChallenge.service;

import com.backend_challenge.backendChallenge.entites.User;
import com.backend_challenge.backendChallenge.repository.RoleRepository;
import com.backend_challenge.backendChallenge.repository.UserRepository;
import com.backend_challenge.backendChallenge.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import request.LoginRequest;
import request.LoginResponse;
import request.RegisterRequest;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {

        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER was not initialized"));


        var newUser = new User();
        newUser.setName(request.name());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setCpf(request.cpf());
        newUser.setBirthDate(request.birthDate());
        newUser.setAddress(request.address());
        newUser.setRoles(List.of(userRole));

        userRepository.save(newUser);
    }

    public LoginResponse login(LoginRequest request) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.name(),
                        request.password()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();

        claims.put("name", user.getName());

        var jwtToken = jwtService.generateToken(claims, user);
        return new LoginResponse(jwtToken);
    }

}
