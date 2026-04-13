package com.example.biblioteca.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.biblioteca.model.usuarios;
import com.example.biblioteca.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificamos si el usuario 'admin' ya existe para no duplicarlo
            if (userRepository.findByUsername("admin").isEmpty()) {
                usuarios admin = new usuarios();
                admin.setUsername("admin");
                // Encriptamos la contraseña por defecto
                admin.setPassword(passwordEncoder.encode("admin123")); 
                admin.setRole("ROLE_ADMIN"); // Rol exacto que espera Spring Security
                
                userRepository.save(admin);
                System.out.println("Usuario ADMIN creado por defecto (Usuario: admin | Password: admin123)");
            }
        };
    }
}