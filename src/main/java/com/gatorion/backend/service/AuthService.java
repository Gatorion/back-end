package com.gatorion.backend.service;

import com.gatorion.backend.dto.AuthRequestDTO;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService; // 1. Adicionamos a ferramenta do JWT

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService) { // 2. Injetamos no construtor
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    /**
     * Autentica um usuário e retorna um token JWT.
     *
     * @return O token JWT gerado.
     */
    public String autenticar(AuthRequestDTO request) { // 3. O método agora retorna uma String (o token)
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean senhaValida = BCrypt.checkpw(request.getSenha(), usuario.getSenha());
        if (!senhaValida) {
            throw new RuntimeException("Suas credenciais estão incorretas");
        }

        // 4. Pulo dp gato: Em vez de retornar o DTO, geramos e retornamos o token.
        return jwtService.generateToken(usuario);
    }
}