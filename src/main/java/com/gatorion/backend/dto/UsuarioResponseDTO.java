package com.gatorion.backend.dto;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UsuarioResponseDTO {

    private final Long id;
    private String nome;
    private String email;

    public UsuarioResponseDTO(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

}
