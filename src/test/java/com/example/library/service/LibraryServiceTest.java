package com.example.library.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryService libraryService;

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
    }

    @Test
    public void testAddBook_Success() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(null);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book addedBook = libraryService.addBook(book);

        assertThat(addedBook).isNotNull();
        assertThat(addedBook.getIsbn()).isEqualTo("1234567890");
        verify(bookRepository, times(1)).findByIsbn(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testAddBook_DuplicateIsbn() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(book);

        assertThatThrownBy(() -> libraryService.addBook(book))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book with this ISBN already exists");

        verify(bookRepository, times(1)).findByIsbn(anyString());
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void testRemoveBook_Success() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(book);
        doNothing().when(bookRepository).delete(any(Book.class));

        libraryService.removeBook("1234567890");

        verify(bookRepository, times(1)).findByIsbn(anyString());
        verify(bookRepository, times(1)).delete(any(Book.class));
    }

    @Test
    public void testRemoveBook_NotFound() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(null);

        assertThatThrownBy(() -> libraryService.removeBook("1234567890"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");

        verify(bookRepository, times(1)).findByIsbn(anyString());
        verify(bookRepository, times(0)).delete(any(Book.class));
    }

    @Test
    public void testFindBookByTitle() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByTitleIgnoreCase(anyString())).thenReturn(books);

        List<Book> foundBooks = libraryService.findBookByTitle("Test Book");

        assertThat(foundBooks).isNotNull();
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle()).isEqualTo("Test Book");
        verify(bookRepository, times(1)).findByTitleIgnoreCase(anyString());
    }

    @Test
    public void testFindBookByAuthor() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByAuthorIgnoreCase(anyString())).thenReturn(books);

        List<Book> foundBooks = libraryService.findBookByAuthor("Test Author");

        assertThat(foundBooks).isNotNull();
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getAuthor()).isEqualTo("Test Author");
        verify(bookRepository, times(1)).findByAuthorIgnoreCase(anyString());
    }

    @Test
    public void testListAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> allBooks = libraryService.listAllBooks();

        assertThat(allBooks).isNotNull();
        assertThat(allBooks).hasSize(1);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testListAvailableBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> availableBooks = libraryService.listAvailableBooks();

        assertThat(availableBooks).isNotNull();
        assertThat(availableBooks).hasSize(1);
        verify(bookRepository, times(1)).findAll();
    }
}
