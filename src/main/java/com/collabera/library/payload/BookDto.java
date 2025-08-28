package com.collabera.library.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDto {
    private Long id;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "ISBN must be 10 or 13 digits")
    private String isbn;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    private Boolean isAvailable;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
