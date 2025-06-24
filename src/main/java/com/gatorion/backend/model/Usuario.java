package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true)
    private String email;

    @Column(length = 60) // Para armazenar senhas criptografadas
    private String senha;

    @Column(unique = true)
    private String nomeUsuario;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Lob
    @Column(columnDefinition = "LONGBLOB") // Especifica o tipo para o MySQL
    private byte[] avatar;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] banner;

    private long xp;
    private int nivel = 1;
}