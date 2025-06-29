package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Usuario")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @EqualsAndHashCode.Include
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
  
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
  
    @Column(name = "endereco", nullable = true)
    private String endereco;

    @Column(name = "experiencia")
    private long xp;

    @Column(name = "nivel")
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