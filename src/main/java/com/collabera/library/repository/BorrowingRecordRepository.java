package com.collabera.library.repository;


import com.collabera.library.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookIdAndIsActive(Long bookId, Boolean isActive);
    Optional<BorrowingRecord> findByBorrowerIdAndBookIdAndIsActive(Long borrowerId, Long bookId, Boolean isActive);
}