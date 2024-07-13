package com.example.library.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.model.Book;
import com.example.library.service.LibraryService;
import com.example.library.validator.BookValidator;;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;
    
    @Autowired
    private BookValidator bookValidator;

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        // Perform programmatic validation
        Errors errors = new BeanPropertyBindingResult(book, "book");
        bookValidator.validate(book, errors);

        if (errors.hasErrors()) {
            // Handle validation errors
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        // If validation passes, add the book to the library
        libraryService.addBook(book);
        return ResponseEntity.ok("Book added successfully");
    }


    @DeleteMapping("/removeBook/{isbn}")
    public ResponseEntity<?> removeBook(@PathVariable String isbn) {
        try {
            libraryService.removeBook(isbn);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findBookByTitle/{title}")
    public ResponseEntity<List<Book>> findBookByTitle(@PathVariable String title) {
        return new ResponseEntity<>(libraryService.findBookByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/findBookByAuthor/{author}")
    public ResponseEntity<List<Book>> findBookByAuthor(@PathVariable String author) {
        return new ResponseEntity<>(libraryService.findBookByAuthor(author), HttpStatus.OK);
    }

    @GetMapping("/listAllBooks")
    public ResponseEntity<List<Book>> listAllBooks() {
        return new ResponseEntity<>(libraryService.listAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/listAvailableBooks")
    public ResponseEntity<List<Book>> listAvailableBooks() {
        return new ResponseEntity<>(libraryService.listAvailableBooks(), HttpStatus.OK);
    }
}
