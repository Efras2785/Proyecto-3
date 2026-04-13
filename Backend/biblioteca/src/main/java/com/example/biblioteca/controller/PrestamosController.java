package com.example.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.model.Prestamos;
import com.example.biblioteca.service.PrestamosService;

@RestController
@RequestMapping("/api/prestamos")

public class PrestamosController {
    @Autowired
    private PrestamosService prestamosService;

    // Usuarios pueden hacer préstamos
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/borrow")
    public Prestamos borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        // En una app real con JWT, el userId se saca del token de seguridad, no de los parámetros
        return prestamosService.borrowBook(userId, bookId);
    }

    // Usuarios pueden ver su propio historial
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/history/{userId}")
    public List<Prestamos> getHistory(@PathVariable Long userId) {
        return prestamosService.getUserHistory(userId);
    }

    // El admin puede ver todos los movimientos
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<Prestamos> getAllHistory() {
        return prestamosService.getAllHistory();
    }
}
