package com.example.biblioteca.repository;

import com.example.biblioteca.model.Prestamos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamosRepository extends JpaRepository<Prestamos, Long> {
    List<Prestamos> findByUserId(Long userId);
}