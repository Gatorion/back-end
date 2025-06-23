package com.gatorion.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public class MaterialRequestDTO {

    @NotNull(message = "Nome do material não pode ser nulo")
    private String nome;

    @NotNull(message = "Conteudo do material não pode ser nulo")
    private String conteudo;

}
