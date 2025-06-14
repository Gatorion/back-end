package com.gatorion.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // O @AllArgsConstructor é usado para gerar um construtor com todos os campos
public class AuthResponseDTO {
    private Long id;
    private String nome;
    private String email;
}
