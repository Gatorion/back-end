package com.gatorion.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "Usuario")
public class Usuario {

    @Id
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

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Lob
    @Column(columnDefinition = "LONGBLOB") // Especifica o tipo para o MySQL
    private byte[] avatar;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] banner;


    @Column(name = "endereco", nullable = true)
    private String endereco;

    private Long xp;
    @Column(name = "nivel")
    private int nivel = 1;

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuario usuario = (Usuario) o;
        return getId() != null && Objects.equals(getId(), usuario.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}