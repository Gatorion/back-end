package com.gatorion.backend.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponseDTO {
    private final Long id;
    private String nome;
    private String email;
    private String nomeUsuario;
    private long xp;
    private int nivel;
}