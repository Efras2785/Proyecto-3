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

    // 1. Obtener mis préstamos de forma segura (usando el username)
    public List<Prestamos> getMyHistory(String username) {
        usuarios user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return prestamosRepository.findByUserId(user.getId());
    }

    // 2. Lógica para devolver un libro
    @Transactional
    public Prestamos returnBook(Long prestamoId, String username) {
        Prestamos prestamo = prestamosRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        // Verificamos que el usuario que intenta devolverlo sea el dueño del préstamo
        if (!prestamo.getUser().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para devolver este libro");
        }

        if ("DEVUELTO".equals(prestamo.getStatus())) {
            throw new RuntimeException("Este libro ya fue devuelto");
        }

        // 1. Marcamos el préstamo como DEVUELTO
        prestamo.setStatus("DEVUELTO");

        // 2. Volvemos a poner el libro como disponible en el catálogo
        Book book = prestamo.getBook();
        book.setDisponible(true);
        bookRepository.save(book);

        return prestamosRepository.save(prestamo);
    }
}
