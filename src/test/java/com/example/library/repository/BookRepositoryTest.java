package com.example.library.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setGenre("Test Genre");
        book.setPublicationYear(2021);
        book.setDepartment("Test Department");
        book.setAvailability(true);
        bookRepository.save(book);
    }

    @Test
    public void testFindByTitleIgnoreCase() {
        List<Book> foundBooks = bookRepository.findByTitleIgnoreCase("test book");
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle()).isEqualTo("Test Book");
    }

    @Test
    public void testFindByAuthorIgnoreCase() {
        List<Book> foundBooks = bookRepository.findByAuthorIgnoreCase("test author");
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getAuthor()).isEqualTo("Test Author");
    }

    @Test
    public void testFindByIsbn() {
        Book foundBook = bookRepository.findByIsbn("1234567890");
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getIsbn()).isEqualTo("1234567890");
    }

    @Test
    public void testSave() {
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setIsbn("0987654321");
        newBook.setGenre("New Genre");
        newBook.setPublicationYear(2022);
        newBook.setDepartment("New Department");
        newBook.setAvailability(true);
        
        Book savedBook = bookRepository.save(newBook);
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");
    }

    @Test
    public void testDelete() {
        Book bookToDelete = bookRepository.findByIsbn("1234567890");
        bookRepository.delete(bookToDelete);

        Book deletedBook = bookRepository.findByIsbn("1234567890");
        assertThat(deletedBook).isNull();
    }

    @Test
    public void testFindAll() {
        List<Book> allBooks = bookRepository.findAll();
        assertThat(allBooks).hasSize(1);
    }
}
