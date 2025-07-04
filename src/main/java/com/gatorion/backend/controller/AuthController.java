package com.gatorion.backend.controller;

import com.gatorion.backend.dto.AuthRequestDTO;
import com.gatorion.backend.dto.AuthResponseDTO;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.UsuarioRepository;
import com.gatorion.backend.service.AuthService;
import com.gatorion.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        try {
            System.out.println("🔐 AuthController: Tentativa de login para: " + request.getEmail());

            // ✅ Usar seu AuthService existente para autenticar e obter o token
            String token = authService.autenticar(request);

            // ✅ Decodificar o token para obter o userId
            Integer userId = jwtService.extractUserIdAsInteger(token);

            // ✅ Buscar dados completos do usuário no banco
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(Long.valueOf(userId));

            if (usuarioOpt.isEmpty()) {
                System.err.println("❌ AuthController: Usuário não encontrado após autenticação");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Erro interno: usuário não encontrado");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

            Usuario usuario = usuarioOpt.get();

            System.out.println("✅ AuthController: Usuário autenticado: " + usuario.getEmail());

            // ✅ Criar resposta com dados completos do usuário
            AuthResponseDTO authResponse = new AuthResponseDTO(
                    usuario.getId(),
                    usuario.getNome(),        // ← Nome completo "Rafael Rios"
                    usuario.getEmail(),       // ← Email "rafaelrios@gmail.com"
                    usuario.getNomeUsuario(), // ← Username "r.rios"
                    usuario.getXp() != 0 ? usuario.getXp() : 0,     // ✅ XP com fallback
                    usuario.getNivel() != 0 ? usuario.getNivel() : 1 // ✅ Nível com fallback
            );

            System.out.println("📋 AuthController: Dados do usuário processados:");
            System.out.println("   ID: " + authResponse.getId());
            System.out.println("   Nome: " + authResponse.getNome());
            System.out.println("   Email: " + authResponse.getEmail());
            System.out.println("   Username: " + authResponse.getNomeUsuario());
            System.out.println("   XP: " + authResponse.getXp());
            System.out.println("   Nível: " + authResponse.getNivel());

            // ✅ Estrutura de resposta padronizada
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", authResponse); // ← Dados completos aqui

            System.out.println("✅ AuthController: Login bem-sucedido para: " + usuario.getEmail());

            return ResponseEntity.ok(response);

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            System.err.println("❌ AuthController: Credenciais inválidas para: " + request.getEmail());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Credenciais inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            System.err.println("❌ AuthController: Erro inesperado: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}