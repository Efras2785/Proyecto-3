package com.example.biblioteca.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Prestamos")

public class Prestamos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private usuarios user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime loanDate;
    
    // "ACTIVO", "DEVUELTO"
    private String status;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public usuarios getUser() {return user;}
    public void setUser(usuarios user) {this.user = user;}
    public Book getBook() {return book;}
    public void setBook(Book book) {this.book = book;}
    public LocalDateTime getLoanDate() {return loanDate;}
    public void setLoanDate(LocalDateTime loanDate) {this.loanDate = loanDate;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

}
