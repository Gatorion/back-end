package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conteudo", length = 300, nullable = false)
    private String conteudo;

    @Lob
    @Column(name = "imagem")
    private byte[] imagem;

    @Column(name = "data_criacao") // ← Mudado de data_postagem
    @CreationTimestamp
    private LocalDateTime dataCriacao; // ← Mudado de dataPostagem

    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    private Usuario autor;
}
