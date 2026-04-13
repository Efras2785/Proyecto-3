package com.example.biblioteca.controller;

import java.io.IOException;
import java.util.List;

// --- IMPORTACIONES CORREGIDAS ---
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType; // <- La librería correcta para Spring
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.biblioteca.model.Book;
import com.example.biblioteca.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper; // <- La librería correcta para Jackson

@RestController
@RequestMapping("/api/libros")
public class BookController {

    @Autowired
    private BookService bookService;

    // Públicos o para usuarios logueados
    @GetMapping
    public List<Book> getAll() {
        return bookService.getAllBooks();
    }

    @GetMapping("/available")
    public List<Book> getAvailable() {
        return bookService.getAvailableBooks();
    }

    // Solo ADMIN puede modificar
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    // Solo ADMIN puede crear el libro CON imagen
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book createBook(
            @RequestPart("book") String bookJson, // El JSON del libro como String
            @RequestPart("file") MultipartFile file // El archivo de la imagen
    ) throws IOException {
        
        // Convertir el String bookJson a objeto Book
        ObjectMapper objectMapper = new ObjectMapper();
        Book book = objectMapper.readValue(bookJson, Book.class);
        
        return bookService.saveBookWithImage(book, file);
    }
}