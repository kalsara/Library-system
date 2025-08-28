package com.collabera.library;

import com.collabera.library.Exception.ResourceNotFoundException;
import com.collabera.library.Exception.ValidationException;
import com.collabera.library.model.Borrower;
import com.collabera.library.payload.BorrowerDto;
import com.collabera.library.repository.BorrowerRepository;
import com.collabera.library.service.BorrowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    private BorrowerDto borrowerDto;
    private Borrower borrower;

    @BeforeEach
    void setUp() {
        borrowerDto = new BorrowerDto();
        borrowerDto.setName("John Doe");
        borrowerDto.setEmail("john@example.com");

        borrower = new Borrower();
        borrower.setId(1L);
        borrower.setName("John Doe");
        borrower.setEmail("john@example.com");
    }

    @Test
    void registerBorrower_Success() {
        // Given
        when(borrowerRepository.existsByEmail(borrowerDto.getEmail())).thenReturn(false);
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(borrower);

        // When
        BorrowerDto result = borrowerService.registerBorrower(borrowerDto);

        // Then
        assertNotNull(result);
        assertEquals(borrower.getId(), result.getId());
        assertEquals(borrower.getName(), result.getName());
        assertEquals(borrower.getEmail(), result.getEmail());
        verify(borrowerRepository).existsByEmail(borrowerDto.getEmail());
        verify(borrowerRepository).save(any(Borrower.class));
    }

    @Test
    void registerBorrower_EmailAlreadyExists() {
        // Given
        when(borrowerRepository.existsByEmail(borrowerDto.getEmail())).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> borrowerService.registerBorrower(borrowerDto));
        assertEquals("Email already exists: " + borrowerDto.getEmail(), exception.getMessage());
        verify(borrowerRepository).existsByEmail(borrowerDto.getEmail());
        verify(borrowerRepository, never()).save(any(Borrower.class));
    }

    @Test
    void getBorrowerById_Success() {
        // Given
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));

        // When
        BorrowerDto result = borrowerService.getBorrowerById(1L);

        // Then
        assertNotNull(result);
        assertEquals(borrower.getId(), result.getId());
        assertEquals(borrower.getName(), result.getName());
        assertEquals(borrower.getEmail(), result.getEmail());
        verify(borrowerRepository).findById(1L);
    }

    @Test
    void getBorrowerById_NotFound() {
        // Given
        when(borrowerRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> borrowerService.getBorrowerById(1L));
        assertEquals("Borrower not found with id: 1", exception.getMessage());
        verify(borrowerRepository).findById(1L);
    }
}