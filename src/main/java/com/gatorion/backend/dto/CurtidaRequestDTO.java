package com.gatorion.backend.dto;

import jakarta.validation.constraints.NotNull;


public class CurtidaRequestDTO {

    @NotNull(message = "O ID do usuario não pode estar vazio")
    private Long idUsuario;

    @NotNull(message = "O ID do Post não pode estar vazio")
    private Long idPost;
}
