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
}
