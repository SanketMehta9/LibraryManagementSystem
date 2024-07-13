package com.example.library.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.library.model.Book;


@Component
public class BookValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        // Example custom validation logic
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            errors.rejectValue("title", "book.title.required", "Title is required");
        }

        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            errors.rejectValue("author", "book.author.required", "Author is required");
        }

        // Add more validation rules as needed
    }
}
