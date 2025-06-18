package com.gatorion.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequestDTO {

    @NotBlank(message = "O conteúdo do post não pode estar vazio")
    private String conteudo;

    @NotNull(message = "O ID do autor não pode estar vazio")
    private Long idAutor;
}
