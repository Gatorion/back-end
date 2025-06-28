package com.gatorion.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor // Construtor sem argumentos que o JPA precisa
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O texto do comentário não pode estar vazio.")
    @Column(columnDefinition = "TEXT")
    private String conteudo;

    // Relacionamento com o autor do comentário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autor;

    // Relacionamento com o post que foi comentado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Um comentário pode ter um "pai" (se for uma resposta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_pai_id") // Nova coluna no banco
    private Comentario comentarioPai;

    // Um comentário pode ter uma lista de "filhos" (suas respostas)
    @OneToMany(mappedBy = "comentarioPai")
    private List<Comentario> respostas;

    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}