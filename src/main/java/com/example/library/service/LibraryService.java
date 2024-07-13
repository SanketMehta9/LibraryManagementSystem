package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()) != null) {
            throw new RuntimeException("Book with this ISBN already exists");
        }
        return bookRepository.save(book);
    }

    public void removeBook(String ISBN) {
        Book book = bookRepository.findByIsbn(ISBN);
        if (book == null) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.delete(book);
    }

    public List<Book> findBookByTitle(String title) {
        return bookRepository.findByTitleIgnoreCase(title);
    }

    public List<Book> findBookByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author);
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> listAvailableBooks() {
        return bookRepository.findAll().stream()
                .filter(Book::isAvailability)
                .toList();
    }
}
