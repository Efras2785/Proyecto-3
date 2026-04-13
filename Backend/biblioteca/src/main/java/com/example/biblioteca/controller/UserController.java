package com.example.biblioteca.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.model.usuarios;
import com.example.biblioteca.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // El ADMIN puede crear nuevos usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public usuarios register(@RequestBody usuarios user) {
        return userService.registerUser(user);
    }
    
    // Aquí también irían los endpoints de Login para devolver el JWT
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    //     // Lógica de inicio de sesión
    // }

}