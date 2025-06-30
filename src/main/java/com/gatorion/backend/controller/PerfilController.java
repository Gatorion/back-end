package com.gatorion.backend.controller;

import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint PÚBLICO para ver o perfil de qualquer usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getPerfilPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        // Futuramente, podemos criar um DTO de resposta para não expor a senha
        return ResponseEntity.ok(usuario);
    }

    // Endpoint que SERÁ PROTEGIDO pelo Spring Security
    @PutMapping("/{id}")
    private ResponseEntity<String> updatePerfil(
            @PathVariable Long id,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
            @RequestParam(value = "banner", required = false) MultipartFile bannerFile,
            @RequestParam("nome") String nome,
            @RequestParam("bio") String bio) {
        try {
            usuarioService.atualizarPerfil(id, nome, bio, avatarFile, bannerFile);
            return ResponseEntity.ok("Perfil atualizado com sucesso!");
        } catch (IOException erro) {
            return ResponseEntity.status(500).body("Erro ao processar imagem: " + erro.getMessage());
        }
    }
}