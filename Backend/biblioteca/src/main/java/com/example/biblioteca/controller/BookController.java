package com.example.biblioteca.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType; 
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
import com.example.biblioteca.service.SupabaseStorageService; // <- Importamos nuestro mensajero
import com.fasterxml.jackson.databind.ObjectMapper; 

@RestController
@RequestMapping("/api/libros")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private SupabaseStorageService storageService; // <- Inyectamos el servicio de Supabase

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
        
        // 1. Convertir el String bookJson a objeto Book usando tu solución
        ObjectMapper objectMapper = new ObjectMapper();
        Book book = objectMapper.readValue(bookJson, Book.class);
        
        // 2. Subir la imagen a Supabase Storage y obtener el link público
        String imageUrl = storageService.uploadImage(file);
        
        // 3. Asignar el link de la imagen a nuestro libro
        book.setPortada(imageUrl);
        
        // 4. Guardar todo (datos + link de la imagen) en la base de datos PostgreSQL
        // Nota: Asegúrate de que tu BookService tenga el método saveBook(book) normal
        return bookService.saveBook(book); 
    }
}