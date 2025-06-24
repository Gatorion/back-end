package com.gatorion.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilUpdateRequestDTO {
    private String nome;
    private String bio;
    private String avatar;
    private String banner;
}
