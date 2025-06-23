package com.gatorion.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequestDTO {

    @NotNull(message = "O ID do autor não pode estar vazio")
    private Long idAutor;

    //conteudo escrito limitado para um post
    @NotBlank(message = "Conteudo não pode ser vazio")
    @Size(max = 300, message = "Limite de caracteres ultrapassado")
    private String conteudo;

}
