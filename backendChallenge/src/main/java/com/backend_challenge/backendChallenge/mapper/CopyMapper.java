package com.backend_challenge.backendChallenge.mapper;

import com.backend_challenge.backendChallenge.dtos.CopyResponse;
import com.backend_challenge.backendChallenge.entites.Copy;
import org.springframework.stereotype.Service;

@Service
public class CopyMapper {

    public CopyResponse toCopyResponse(Copy copy) {

        var response = new CopyResponse(
                copy.getId(),
                copy.getTitle(),
                copy.isAvailable()
        );

        return response;
    }

}
