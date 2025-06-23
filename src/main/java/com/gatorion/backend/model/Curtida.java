package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Curtida")
public class Curtida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //curtida é ligada ao usuario que curtiu
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    //curtida é ligada ao post curtido
    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;

    @Column(name = "dataCurtida", nullable = false)
    private LocalDateTime dataCurtida;

    @PrePersist //salva a data automaticamente quando cria o post
    protected void onCreate() {this.dataCurtida = LocalDateTime.now();}
}
