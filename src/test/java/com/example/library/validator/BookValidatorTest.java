package com.example.library.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidatorTest {

    private BookValidator bookValidator;
    private Book book;
    private Errors errors;

    @BeforeEach
    public void setUp() {
        bookValidator = new BookValidator();
        book = new Book();
        errors = new BeanPropertyBindingResult(book, "book");
    }

    @Test
    public void testSupports() {
        assertThat(bookValidator.supports(Book.class)).isTrue();
        assertThat(bookValidator.supports(Object.class)).isFalse();
    }

    @Test
    public void testValidate_Success() {
        book.setTitle("Test Title");
        book.setAuthor("Test Author");

        bookValidator.validate(book, errors);

        assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_TitleRequired() {
        book.setAuthor("Test Author");

        bookValidator.validate(book, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getFieldError("title").getCode()).isEqualTo("book.title.required");
        assertThat(errors.getFieldError("title").getDefaultMessage()).isEqualTo("Title is required");
    }

    @Test
    public void testValidate_AuthorRequired() {
        book.setTitle("Test Title");

        bookValidator.validate(book, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getFieldError("author").getCode()).isEqualTo("book.author.required");
        assertThat(errors.getFieldError("author").getDefaultMessage()).isEqualTo("Author is required");
    }

    @Test
    public void testValidate_TitleAndAuthorRequired() {
        bookValidator.validate(book, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getFieldError("title").getCode()).isEqualTo("book.title.required");
        assertThat(errors.getFieldError("title").getDefaultMessage()).isEqualTo("Title is required");
        assertThat(errors.getFieldError("author").getCode()).isEqualTo("book.author.required");
        assertThat(errors.getFieldError("author").getDefaultMessage()).isEqualTo("Author is required");
    }
}
