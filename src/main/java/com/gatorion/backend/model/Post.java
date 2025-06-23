package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "dataCriacao", nullable = false)
    private LocalDateTime dataCriacao;

    //Conecta o id co usuario ao post criado por ele
    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    private Usuario autor;

    @PrePersist //salva a data automaticamente quando cria o post
    protected void onCreate() {this.dataCriacao = LocalDateTime.now();}

}
