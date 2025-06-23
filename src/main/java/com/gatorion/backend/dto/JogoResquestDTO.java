package com.gatorion.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class JogoResquestDTO {

    @NotBlank(message = "O nome não pode ficar em branco")
    private String nome;

    @NotBlank(message = "Descrição do jogo não pode ser em branco")
    private String descricao;

    //por ser algo da plataforma, link pode ser de tamanho variado
    @URL(message = "URL inválido")
    private String url;

}
