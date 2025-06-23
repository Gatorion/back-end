package com.gatorion.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioRequestDTO {

    @NotBlank(message = "Por favor, digite um nome")
    @Size(min = 3, max = 100, message = "O nome deve ter no minimo 3 caracteres")
    private String nome;

    @NotBlank(message = "Por favor, preencha o campo e-mail")
    @Email(message = "O e-mail invalido") //valida se o valor tem um formato de email
    private String email;

    @NotBlank(message = "Digite uma senha")
    @Size(min = 8, message = "Digite uma senha com no minimo 8 caracteres")
    private String senha;

}