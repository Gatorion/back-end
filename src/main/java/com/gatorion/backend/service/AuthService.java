package com.gatorion.backend.service;

import com.gatorion.backend.dto.AuthRequestDTO;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.UsuarioRepository;
import com.gatorion.backend.security.UsuarioDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**

     Autentica um usuário e retorna um token JWT.
     @return O token JWT gerado.*/
    public String autenticar(AuthRequestDTO request) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getSenha());

        Authentication authResult = authenticationManager.authenticate(authRequest);

        // Correção aqui:
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authResult.getPrincipal();
        Usuario usuario = usuarioDetails.getUsuario();

        return jwtService.generateToken(usuario);
    }
}