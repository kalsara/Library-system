package com.collabera.library.controller;

import com.collabera.library.payload.BorrowerDto;
import com.collabera.library.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    public ResponseEntity<BorrowerDto> registerBorrower(@Valid @RequestBody BorrowerDto borrowerDto) {
        BorrowerDto savedBorrower = borrowerService.registerBorrower(borrowerDto);
        return new ResponseEntity<>(savedBorrower, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowerDto> getBorrowerById(@PathVariable Long id) {
        BorrowerDto borrower = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(borrower);
    }
}