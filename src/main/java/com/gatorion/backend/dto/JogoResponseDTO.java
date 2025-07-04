package com.gatorion.backend.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JogoResponseDTO {
    private Long id;
    private String nome;
    private String url;
    private String descricao;
}
