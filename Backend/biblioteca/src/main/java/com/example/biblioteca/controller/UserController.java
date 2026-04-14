package com.example.biblioteca.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping("/register")
    public usuarios register(@RequestBody usuarios user) {
        return userService.registerUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<usuarios> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public usuarios updateUser(@org.springframework.web.bind.annotation.PathVariable Long id, @RequestBody usuarios userDetails) {
        return userService.updateUser(id, userDetails);
    }

    
    // Aquí también irían los endpoints de Login para devolver el JWT
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    //     // Lógica de inicio de sesión
    // }

}