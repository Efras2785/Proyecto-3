package com.example.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.biblioteca.model.usuarios;
import com.example.biblioteca.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectado desde tu SecurityConfig

    public usuarios registerUser(usuarios user) {
        // Encriptar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Si no se especifica rol, asignar USER por defecto
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        return userRepository.save(user);
    }

    public List<usuarios> getAllUsers() {
        // userRepository ya tiene el método findAll() por defecto gracias a Spring Data JPA
        return userRepository.findAll();
    }
    public usuarios updateUser(Long id, usuarios userDetails) {
        usuarios usuarioExistente = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Actualizamos solo el nombre y el rol (la contraseña no la tocamos aquí por seguridad)
        usuarioExistente.setUsername(userDetails.getUsername());
        usuarioExistente.setRole(userDetails.getRole());
        
        return userRepository.save(usuarioExistente);
    }
}