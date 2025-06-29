package com.gatorion.backend.dto;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeguidorRequestDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]+(?:[.][a-zA-Z0-9]+)*$",
            message = "O nome de usuário não pode conter espaços ou caracteres especiais")
    private String influecer;
    @Pattern(regexp = "^[a-zA-Z0-9]+(?:[.][a-zA-Z0-9]+)*$",
            message = "O nome de usuário não pode conter espaços ou caracteres especiais")
    private String seguidor;
}