package com.gatorion.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CurtidaRequestDTO {

    @NotNull(message = "O ID do usuario não pode estar vazio")
    private Long idUsuario;

    @NotNull(message = "O ID do Post não pode estar vazio")
    private Long idPost;
}
