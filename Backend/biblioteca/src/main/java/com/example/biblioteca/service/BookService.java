package com.example.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.biblioteca.model.Book;
import com.example.biblioteca.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByIsAvailableTrue();
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        book.setTitulo(bookDetails.getTitulo());
        book.setAutor(bookDetails.getAutor());
        book.setGenero(bookDetails.getGenero());
        book.setDescripcion(bookDetails.getDescripcion());
        book.setPortada(bookDetails.getPortada());
        book.setDisponible(bookDetails.isDisponible());
        return bookRepository.save(book);
    }

    public Book saveBookWithImage(Book book, MultipartFile file) {
        // Verificamos que sí se haya enviado un archivo
        if (file != null && !file.isEmpty()) {
            
            // 1. Aquí iría el código real para conectar a Supabase Storage.
            // Por ahora, simulamos la URL que te daría Supabase usando el nombre del archivo.
            String nombreArchivo = file.getOriginalFilename();
            String baseUrl = "https://vrptrvtkgzkpetxzrclg.supabase.co/storage/v1/object/public/portadas/";
            String urlImagen = baseUrl + nombreArchivo;
            
            // 2. Le asignamos esa URL al atributo 'portada' de tu libro
            book.setPortada(urlImagen);
        }
        
        // 3. Guardamos en la base de datos (se guardará la URL, no el archivo pesado)
        return bookRepository.save(book);
    }
}