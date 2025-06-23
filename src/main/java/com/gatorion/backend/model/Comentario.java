package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conteudo", length = 300, nullable = false)
    private String conteudo;

    @Column(name = "dataComentario", nullable = false)
    private LocalDateTime dataComentario;

    @PrePersist //salva a data automaticamente quando cria o post
    protected void onCreate() {this.dataComentario = LocalDateTime.now();}

    //comentario ligado ao usuario que fez comentario
    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    private Usuario autor;

    //comentario ligado ao post que foi comentado
    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;

}
