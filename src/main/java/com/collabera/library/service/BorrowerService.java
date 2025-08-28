package com.collabera.library.service;


import com.collabera.library.Exception.ResourceNotFoundException;
import com.collabera.library.model.Borrower;
import com.collabera.library.repository.BorrowerRepository;
import com.collabera.library.payload.BorrowerDto;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public BorrowerDto registerBorrower(BorrowerDto borrowerDto) {
        if (borrowerRepository.existsByEmail(borrowerDto.getEmail())) {
            throw new ValidationException("Email already exists: " + borrowerDto.getEmail());
        }

        Borrower borrower = new Borrower();
        borrower.setName(borrowerDto.getName());
        borrower.setEmail(borrowerDto.getEmail());

        Borrower savedBorrower = borrowerRepository.save(borrower);
        return mapToDto(savedBorrower);
    }

    @Transactional(readOnly = true)
    public BorrowerDto getBorrowerById(Long id) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + id));
        return mapToDto(borrower);
    }

    private BorrowerDto mapToDto(Borrower borrower) {
        BorrowerDto dto = new BorrowerDto();
        dto.setId(borrower.getId());
        dto.setName(borrower.getName());
        dto.setEmail(borrower.getEmail());
        dto.setCreatedDate(borrower.getCreatedDate());
        dto.setLastModifiedDate(borrower.getLastModifiedDate());
        return dto;
    }
}