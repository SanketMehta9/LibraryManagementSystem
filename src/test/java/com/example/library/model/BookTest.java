package com.example.library.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.library.controller.LibraryController;
import com.example.library.model.Book;
import com.example.library.service.LibraryService;
import com.example.library.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
public class BookTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    @MockBean
    private BookValidator bookValidator;

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
    public void testAddBook_Success() throws Exception {
        Errors errors = new BeanPropertyBindingResult(book, "book");
        Mockito.doNothing().when(bookValidator).validate(any(Book.class), any(Errors.class));
        Mockito.when(libraryService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/library/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Book\",\"author\":\"Test Author\",\"isbn\":\"1234567890\",\"genre\":\"Test Genre\",\"publicationYear\":2021,\"department\":\"Test Department\",\"available\":true}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added successfully"));
    }

    @Test
    public void testAddBook_ValidationErrors() throws Exception {
        Errors errors = new BeanPropertyBindingResult(book, "book");
        Mockito.doAnswer(invocation -> {
            Errors errs = invocation.getArgument(1);
            errs.reject("title", "Title is required");
            return null;
        }).when(bookValidator).validate(any(Book.class), any(Errors.class));

        mockMvc.perform(post("/library/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"author\":\"Test Author\",\"isbn\":\"1234567890\",\"genre\":\"Test Genre\",\"publicationYear\":2021,\"department\":\"Test Department\",\"available\":true}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].defaultMessage").value("Title is required"));
    }

    @Test
    public void testRemoveBook_Success() throws Exception {
        doNothing().when(libraryService).removeBook(anyString());

        mockMvc.perform(delete("/library/removeBook/1234567890"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveBook_NotFound() throws Exception {
        doThrow(new RuntimeException("Book not found")).when(libraryService).removeBook(anyString());

        mockMvc.perform(delete("/library/removeBook/1234567890"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));
    }

    @Test
    public void testFindBookByTitle() throws Exception {
        List<Book> books = Arrays.asList(book);
        Mockito.when(libraryService.findBookByTitle(anyString())).thenReturn(books);

        mockMvc.perform(get("/library/findBookByTitle/Test Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    public void testFindBookByAuthor() throws Exception {
        List<Book> books = Arrays.asList(book);
        Mockito.when(libraryService.findBookByAuthor(anyString())).thenReturn(books);

        mockMvc.perform(get("/library/findBookByAuthor/Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author").value("Test Author"));
    }

    @Test
    public void testListAllBooks() throws Exception {
        List<Book> books = Arrays.asList(book);
        Mockito.when(libraryService.listAllBooks()).thenReturn(books);

        mockMvc.perform(get("/library/listAllBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    public void testListAvailableBooks() throws Exception {
        List<Book> books = Arrays.asList(book);
        Mockito.when(libraryService.listAvailableBooks()).thenReturn(books);

        mockMvc.perform(get("/library/listAvailableBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }
}

