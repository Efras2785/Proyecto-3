package com.example.biblioteca.config;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // En un entorno real, esta clave debe ir en tu application.properties
    // Debe ser una cadena larga y segura (al menos 256 bits)
    private static final String SECRET = "EstaEsUnaClaveSuperSecretaParaMiBibliotecaDigital2024!";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Tiempo de vida del Access Token (ej. 1 día)
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24;
    
    // Tiempo de vida del Refresh Token (ej. 7 días)
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    public String generateToken(String username) {
        return createToken(username, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_EXPIRATION);
    }

    private String createToken(String subject, long expirationTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}