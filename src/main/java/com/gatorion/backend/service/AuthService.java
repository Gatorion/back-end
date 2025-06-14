package com.gatorion.backend.service;

import com.gatorion.backend.dto.AuthRequestDTO;
import com.gatorion.backend.dto.AuthResponseDTO;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public AuthResponseDTO autenticar(AuthRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        boolean senhaValida = BCrypt.checkpw(request.getSenha(), usuario.getSenha());
        if (!senhaValida) {
            throw new RuntimeException("Suas credenciais estão incorretas");
        }
        
        return new AuthResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
