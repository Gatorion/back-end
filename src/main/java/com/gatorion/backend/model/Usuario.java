package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = false, length = 60) // Para armazenar senhas criptografadas
    private String senha;

    @Column(name == "username", unique = true)
    private String nomeUsuario;
  
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
  
    @Column(name = "endereco", nullable = true)
    private String endereco;
    
    @Column(name = "experiencia")
    private long xp;
    @Column(name = "experiencia")
    private int nivel;
}