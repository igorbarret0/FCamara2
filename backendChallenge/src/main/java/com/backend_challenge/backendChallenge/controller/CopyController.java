package com.backend_challenge.backendChallenge.controller;

import com.backend_challenge.backendChallenge.dtos.CopyRequest;
import com.backend_challenge.backendChallenge.dtos.CopyResponse;
import com.backend_challenge.backendChallenge.dtos.ReturnRentedBookDto;
import com.backend_challenge.backendChallenge.service.CopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/copies")
@RequiredArgsConstructor
public class CopyController {

    private final CopyService copyService;

    @PostMapping
    public ResponseEntity<Void> generateCopy(@RequestBody CopyRequest request, Authentication connectedUser) {

        copyService.generateCopy(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rent/{book-id}")
    public ResponseEntity<String> rentCopyBook(
            @PathVariable(name = "book-id") Long bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(copyService.rentCopyBook(bookId, connectedUser));
    }

    @GetMapping
    public ResponseEntity<List<CopyResponse>> getAllCopies() {

        return ResponseEntity.ok(copyService.getAllCopies());
    }

    @GetMapping("/{copy-id}")
    public ResponseEntity<CopyResponse> getCopyById(
            @PathVariable(name = "copy-id") Long copyId
    ) {

        return ResponseEntity.ok(copyService.getCopyById(copyId));
    }

    @PostMapping("/return")
    public ResponseEntity<Void> returnRentedBook(
            @RequestBody ReturnRentedBookDto request,
            Authentication connectedUser
    ) {

        copyService.returnRentedBook(request.copyCode(), connectedUser);
        return ResponseEntity.accepted().build();
    }

}
