package com.collabera.library.repository;

import com.collabera.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn AND b.title = :title AND b.author = :author")
    List<Book> findByIsbnAndTitleAndAuthor(@Param("isbn") String isbn,
                                           @Param("title") String title,
                                           @Param("author") String author);

    Optional<Book> findByIdAndIsAvailable(Long id, Boolean isAvailable);
}