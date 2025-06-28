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

    @Column(name = "username", unique = true)
    private String nomeUsuario;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Lob
    @Column(columnDefinition = "LONGBLOB") // Especifica o tipo para o MySQL
    private byte[] avatar;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] banner;


    @Column(name = "endereco", nullable = true)
    private String endereco;

    private long xp;
    @Column(name = "nivel")
    private int nivel = 1;
}