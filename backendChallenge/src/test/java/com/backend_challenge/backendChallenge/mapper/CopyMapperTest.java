package com.backend_challenge.backendChallenge.mapper;

import com.backend_challenge.backendChallenge.dtos.CopyResponse;
import com.backend_challenge.backendChallenge.entites.Copy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CopyMapperTest {

    @Test
    public void toCopyResponseTest() {

        Copy copy = new Copy();
        copy.setId(1L);
        copy.setTitle("The Great Gatsby");
        copy.setAvailable(true);

        CopyMapper copyMapper = new CopyMapper();
        CopyResponse copyResponse = copyMapper.toCopyResponse(copy);

        assertNotNull(copyResponse, "The returned object should not be null");
        assertTrue(copyResponse instanceof CopyResponse, "The returned object should be a instance of CopyResponse");

        assertEquals(1L, copyResponse.id());
        assertEquals("The Great Gatsby", copyResponse.title());
        assertTrue(copyResponse.isAvailable());
    }

}
