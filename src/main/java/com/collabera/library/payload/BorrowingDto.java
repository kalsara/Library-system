package com.collabera.library.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowingDto {
    private Long id;
    private Long borrowerId;
    private String borrowerName;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private LocalDateTime borrowedDate;
    private LocalDateTime returnedDate;
    private Boolean isActive;
}
