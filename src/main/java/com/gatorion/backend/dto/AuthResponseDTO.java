package com.gatorion.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de resposta de autenticação.
 * <p>
 * Esta classe é utilizada para retornar informações básicas do usuário
 * após autenticação bem-sucedida, como id, nome e email.
 * </p>
 * @author r-riosp
 */
@Getter
@Setter
@AllArgsConstructor // O @AllArgsConstructor é usado para gerar um construtor com todos os campos
public class AuthResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String nomeUsuario;
    private long xp;
    private int nivel;
}
