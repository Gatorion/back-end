package com.gatorion.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private Long idAutor;
    private String nomeAutor;
    private long totalCurtidas;
    private boolean curtidoPeloUsuario; // Retorna true se o usuário logado curtiu, e false senão
    private long totalComentarios;

}
