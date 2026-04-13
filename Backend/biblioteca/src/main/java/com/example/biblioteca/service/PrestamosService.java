package com.example.biblioteca.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.biblioteca.model.Book;
import com.example.biblioteca.model.Prestamos;
import com.example.biblioteca.model.usuarios;
import com.example.biblioteca.repository.BookRepository;
import com.example.biblioteca.repository.PrestamosRepository;
import com.example.biblioteca.repository.UserRepository;

@Service

public class PrestamosService {
    @Autowired
    private PrestamosRepository prestamosRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional // Asegura que si falla un paso, no se guarde nada a medias
    public Prestamos borrowBook(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        
        if (!book.isDisponible()) {
            throw new RuntimeException("El libro ya está prestado.");
        }

        usuarios user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Prestamos prestamo = new Prestamos();
        prestamo.setUser(user);
        prestamo.setBook(book);
        prestamo.setLoanDate(LocalDateTime.now());
        prestamo.setStatus("ACTIVO");

        // Cambiamos disponibilidad a false
        book.setDisponible(false);
        bookRepository.save(book);

        return prestamosRepository.save(prestamo);
    }

    public List<Prestamos> getUserHistory(Long userId) {
        return prestamosRepository.findByUserId(userId);
    }
    
    public List<Prestamos> getAllHistory() {
        return prestamosRepository.findAll();
    }
}
