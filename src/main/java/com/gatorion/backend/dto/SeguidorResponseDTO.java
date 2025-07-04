package com.gatorion.backend.dto;

import com.gatorion.backend.model.Usuario;

import java.util.List;
//futuramente caso queria passa algo alem do nome do usuário
//mas também o id, para caso clicar no nome dele procurar o perfil dele
//para renderizar através do id
//basta fazer uma DTO de usuárioSeguidor que contem nome e id
//por enquanto no mvp podemos usar apenas uma Lista de String já que vamos utilzar só um nome

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeguidorResponseDTO {
    private List<String> seguidores;
    private List<String> seguindo;
}
