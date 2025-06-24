package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    private long xp;
    private int nivel;

    @ManyToMany
    @JoinTable(
            name = "usuario_seguidores",
            joinColumns = @JoinColumn(name = "usuario_id"),//influencer
            inverseJoinColumns = @JoinColumn(name = "seguidor_id")//seguidor
    )
    @OnDelete(action = OnDeleteAction.CASCADE) //remove todos os relacionamentos caso apaguemos o usuário
    @ToString.Exclude
    private List<Usuario> seguidores = new ArrayList<>();

    @ManyToMany(mappedBy = "seguidores")
    @ToString.Exclude
    private List<Usuario> seguindo = new ArrayList<>();
}