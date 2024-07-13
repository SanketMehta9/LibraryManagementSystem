package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleIgnoreCase(String title);
    List<Book> findByAuthorIgnoreCase(String author);
    Book findByIsbn(String isbn);
}
