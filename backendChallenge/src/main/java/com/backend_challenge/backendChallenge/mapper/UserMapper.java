package com.backend_challenge.backendChallenge.mapper;

import com.backend_challenge.backendChallenge.dtos.UserResponse;
import com.backend_challenge.backendChallenge.entites.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserResponse toUserResponse(User user) {

        var response = new UserResponse(
                user.getName(),
                user.getAddress(),
                user.getDelays()
        );

        return response;
    }

}
