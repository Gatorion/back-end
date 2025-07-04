package com.gatorion.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioRequestDTO {
    @NotBlank(message = "Nome de usuário é obrigatório.")
    @Size(min = 3, message = "O nome de usuário deve ter pelo menos 3 caracteres")
    @Size(max = 20, message = "O nome de usuário deve ter no máximo 20 caracteres")
    // Não é permitido espaços em branco no nome de usuário
    @Pattern(regexp = "^[a-zA-Z0-9]+(?:[._][a-zA-Z0-9]+)*$",
            message = "O nome de usuário não pode conter espaços ou caracteres especiais")
    private String nomeUsuario;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotBlank(message = "Por favor, preencha o campo e-mail")
    @Email(message = "O e-mail invalido") //valida se o valor tem um formato de email
    private String email;


    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String senha;

    private long xp = 0; // Inicializa com 0 XP
}