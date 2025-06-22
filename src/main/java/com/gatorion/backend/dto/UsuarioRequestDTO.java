package com.gatorion.backend.dto;

import jakarta.persistence.Column;
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
    @Column(unique = true)
    @Size(min = 3, message = "O nome de usuário deve ter pelo menos 3 caracteres")
    @Size(max = 20, message = "O nome de usuário deve ter no máximo 20 caracteres")
    // Não é permitido espaços em branco no nome de usuário
    @Pattern(regexp = "^[a-zA-Z0-9]+(?:[._][a-zA-Z0-9]+)*$",
            message = "O nome de usuário não pode conter espaços ou caracteres especiais")
    private String nomeUsuario;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório.")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String senha;
}