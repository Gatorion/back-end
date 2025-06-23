package com.gatorion.backend.model;

import jakarta.persistence.*;


@Entity
@Table(name = "Material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 50, nullable = false, unique = true)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String conteudo;
}
