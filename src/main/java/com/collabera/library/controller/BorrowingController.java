package com.collabera.library.controller;

import com.collabera.library.payload.BorrowingDto;
import com.collabera.library.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/borrowing")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingDto> borrowBook(@RequestParam Long borrowerId, @RequestParam Long bookId) {
        BorrowingDto borrowingRecord = borrowingService.borrowBook(borrowerId, bookId);
        return new ResponseEntity<>(borrowingRecord, HttpStatus.CREATED);
    }

    @PostMapping("/return")
    public ResponseEntity<BorrowingDto> returnBook(@RequestParam Long borrowerId, @RequestParam Long bookId) {
        BorrowingDto borrowingRecord = borrowingService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok(borrowingRecord);
    }
}