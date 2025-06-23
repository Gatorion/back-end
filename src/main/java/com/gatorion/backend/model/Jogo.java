package com.gatorion.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 20)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    //por ser algo da plataforma, link pode ser de tamanho variado
    @Column(name = "link", columnDefinition = "TEXT")
    private String url;
}
