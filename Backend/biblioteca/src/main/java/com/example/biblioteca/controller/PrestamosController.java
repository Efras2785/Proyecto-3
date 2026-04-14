package com.example.biblioteca.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.model.Prestamos;
import com.example.biblioteca.model.usuarios;
import com.example.biblioteca.repository.UserRepository;
import com.example.biblioteca.service.PrestamosService;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamosController {

    @Autowired
    private PrestamosService prestamosService;

    @Autowired
    private UserRepository userRepository; // Lo necesitamos para traducir el Token a un ID

    // Usuarios pueden hacer préstamos
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/borrow")
    public Prestamos borrowBook(@RequestBody Map<String, Object> request, Authentication authentication) {
        
        // 1. Sacamos el nombre de usuario del Token de seguridad
        String username = authentication.getName();
        
        // 2. Buscamos a ese usuario en la BD para obtener su ID numérico
        usuarios user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // 3. Obtenemos el ID del libro que el frontend nos mandó en el JSON
        Long bookId = Long.valueOf(request.get("libroId").toString());
        
        // 4. ¡Llamamos a tu servicio original intacto!
        return prestamosService.borrowBook(user.getId(), bookId);
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

    // Endpoint para ver MIS préstamos (Lee el usuario desde el Token)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mis-prestamos")
    public List<Prestamos> getMisPrestamos(Authentication authentication) {
        return prestamosService.getMyHistory(authentication.getName());
    }

    // Endpoint para devolver el libro
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/return/{prestamoId}")
    public Prestamos returnBook(@PathVariable Long prestamoId, Authentication authentication) {
        return prestamosService.returnBook(prestamoId, authentication.getName());
    }
}