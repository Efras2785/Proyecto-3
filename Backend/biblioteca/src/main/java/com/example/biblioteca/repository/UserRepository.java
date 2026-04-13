package com.example.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.biblioteca.model.usuarios;

@Repository
public interface UserRepository extends JpaRepository<usuarios, Long> {
    Optional<usuarios> findByUsername(String username);
}