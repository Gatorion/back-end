package com.gatorion.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
    private String email;
    private String senha;
}
