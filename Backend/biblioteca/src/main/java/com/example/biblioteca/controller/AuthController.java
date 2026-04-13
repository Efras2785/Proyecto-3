package com.example.biblioteca.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.config.JwtUtil;
import com.example.biblioteca.model.usuarios;
import com.example.biblioteca.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    // Endpoint de Login
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody usuarios loginRequest) {
        // Esto verifica la contraseña con Bcrypt automáticamente usando el AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Si pasa la autenticación, generamos los tokens
        String accessToken = jwtUtil.generateToken(loginRequest.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getUsername());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    // Endpoint para usar el Refresh Token
    @PostMapping("/refresh")
    public Map<String, String> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String username = jwtUtil.extractUsername(refreshToken);

        // Validamos que el refresh token sea válido
        if (jwtUtil.isTokenValid(refreshToken, username)) {
            String newAccessToken = jwtUtil.generateToken(username);
            
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", refreshToken); // Devolvemos el mismo o puedes generar uno nuevo
            return tokens;
        } else {
            throw new RuntimeException("Refresh token inválido o expirado");
        }
    }
}