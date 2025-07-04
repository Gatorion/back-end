package com.gatorion.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentarioRequestDTO {

    @NotBlank(message = "O texto do comentário não pode estar vazio.")
    private String conteudo;

    @NotNull(message = "O ID do post é obrigatório.")
    private Long postId;

    // Futuramente, com o JWT, não precisaremos mais deste campo.
    // Mas para nosso MVP, o frontend nos informará quem é o autor.
    @NotNull(message = "O ID do autor é obrigatório.")
    private Long autorId;
}