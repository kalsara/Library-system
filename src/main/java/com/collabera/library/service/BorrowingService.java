package com.collabera.library.service;

import com.collabera.library.Exception.BookAlreadyBorrowedException;
import com.collabera.library.Exception.ResourceNotFoundException;
import com.collabera.library.model.Book;
import com.collabera.library.model.Borrower;
import com.collabera.library.model.BorrowingRecord;
import com.collabera.library.repository.BookRepository;
import com.collabera.library.repository.BorrowerRepository;
import com.collabera.library.repository.BorrowingRecordRepository;
import com.collabera.library.payload.BorrowingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowingService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

    public BorrowingDto borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (Boolean.FALSE.equals(book.getIsAvailable())) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        // Check if there's an active borrowing record for this book
        if (borrowingRecordRepository.findByBookIdAndIsActive(bookId, true).isPresent()) {
            throw new BookAlreadyBorrowedException("Book is already borrowed by another member");
        }

        // Create borrowing record
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBorrower(borrower);
        borrowingRecord.setBook(book);
        borrowingRecord.setIsActive(true);

        // Update book availability
        book.setIsAvailable(false);

        BorrowingRecord savedRecord = borrowingRecordRepository.save(borrowingRecord);
        bookRepository.save(book);

        return mapToDto(savedRecord);
    }

    public BorrowingDto returnBook(Long borrowerId, Long bookId) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository
                .findByBorrowerIdAndBookIdAndIsActive(borrowerId, bookId, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active borrowing record found for borrower " + borrowerId + " and book " + bookId));

        // Update borrowing record
        borrowingRecord.setReturnedDate(LocalDateTime.now());
        borrowingRecord.setIsActive(false);

        // Update book availability
        Book book = borrowingRecord.getBook();
        book.setIsAvailable(true);

        BorrowingRecord savedRecord = borrowingRecordRepository.save(borrowingRecord);
        bookRepository.save(book);

        return mapToDto(savedRecord);
    }

    private BorrowingDto mapToDto(BorrowingRecord record) {
        BorrowingDto dto = new BorrowingDto();
        dto.setId(record.getId());
        dto.setBorrowerId(record.getBorrower().getId());
        dto.setBorrowerName(record.getBorrower().getName());
        dto.setBookId(record.getBook().getId());
        dto.setBookTitle(record.getBook().getTitle());
        dto.setBookAuthor(record.getBook().getAuthor());
        dto.setBorrowedDate(record.getBorrowedDate());
        dto.setReturnedDate(record.getReturnedDate());
        dto.setIsActive(record.getIsActive());
        return dto;
    }
}