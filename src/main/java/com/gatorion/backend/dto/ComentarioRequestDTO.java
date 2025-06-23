package com.gatorion.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ComentarioRequestDTO {

    @NotNull(message = "O ID do Post não pode estar vazio")
    private Long idPost;

    @NotNull(message = "O ID do Autor não pode estar vazio")
    private Long idAutor;

    //criterios para comentar iguais a postagem
    @NotBlank(message = "Conteudo não pode ser vazio")
    @Size(max = 300, message = "Limite de caracteres ultrapassado")
    private String conteudo;

}
