package com.collabera.library.service;

import com.collabera.library.repository.BookRepository;
import com.collabera.library.Exception.ResourceNotFoundException;
import com.collabera.library.model.Book;
import com.collabera.library.payload.BookDto;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookDto registerBook(BookDto bookDto) {
        // Validate ISBN consistency
        List<Book> existingBooksWithISBN = bookRepository.findByIsbn(bookDto.getIsbn());

        for (Book existingBook : existingBooksWithISBN) {
            if (!existingBook.getTitle().equals(bookDto.getTitle()) ||
                    !existingBook.getAuthor().equals(bookDto.getAuthor())) {
                throw new ValidationException(
                        "ISBN " + bookDto.getIsbn() + " already exists with different title/author");
            }
        }

        Book book = new Book();
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsAvailable(true);

        Book savedBook = bookRepository.save(book);
        return mapToDto(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToDto(book);
    }

    private BookDto mapToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsAvailable(book.getIsAvailable());
        dto.setCreatedDate(book.getCreatedDate());
        dto.setLastModifiedDate(book.getLastModifiedDate());
        return dto;
    }
}