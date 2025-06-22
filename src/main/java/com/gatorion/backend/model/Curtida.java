package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor // Construtor sem argumentos para o JPA
public class Curtida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A chave para sabermos QUEM curtiu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // A chave para sabermos O QUE foi curtido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Um bônus: saber QUANDO foi curtido
    // Normalmente nós usuários não sabemos as datas das curtidas,
    // mas é melhor deixarmos por questão de segurança
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    // Construtor customizado para facilitar a criação
    public Curtida(Usuario usuario, Post post) {
        this.usuario = usuario;
        this.post = post;
    }
}