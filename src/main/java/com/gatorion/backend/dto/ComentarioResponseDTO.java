package com.gatorion.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ComentarioResponseDTO {
    private Long id;
    private String conteudo;
    private LocalDateTime dataComentario;
    private Long idAutor;
    private String nomeAutor;
}
