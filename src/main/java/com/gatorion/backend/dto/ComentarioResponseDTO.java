package com.gatorion.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ComentarioResponseDTO {

    private Long id;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private Long autorId;
    private String autorNome;
    // Poderíamos adicionar o @nomeUsuario e a foto de perfil do autor no futuro
    private String autorNomeUsuario;
}