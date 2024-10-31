package com.backend_challenge.backendChallenge.mapper;

import com.backend_challenge.backendChallenge.dtos.UserResponse;
import com.backend_challenge.backendChallenge.entites.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    @Test
    public void toUserResponseTest() {

        User user = new User();
        user.setName("Robert Downey Jr");
        user.setAddress("Some place in USA");
        user.setDelays(1);

        UserMapper userMapper = new UserMapper();
        UserResponse userResponse = userMapper.toUserResponse(user);

        assertNotNull(userResponse, "The returned object should not be null");
        assertTrue(userResponse instanceof UserResponse, "The returned object should be a instance of UserResponse");

        assertEquals("Robert Downey Jr", userResponse.name());
        assertEquals("Some place in USA", userResponse.address());
        assertEquals(1, userResponse.delays());
    }

}
