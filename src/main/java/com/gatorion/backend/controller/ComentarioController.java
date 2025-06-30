package com.gatorion.backend.controller;

import com.gatorion.backend.dto.ComentarioRequestDTO;
import com.gatorion.backend.dto.ComentarioResponseDTO;
import com.gatorion.backend.security.UsuarioDetailsImpl;
import com.gatorion.backend.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ComentarioController {

    private final ComentarioService comentarioService;

    @Autowired
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    /**
     * Endpoint para criar um novo comentário em um post.
     * O autor é identificado pelo token JWT.
     */
    @PostMapping("/comentarios")
    public ResponseEntity<ComentarioResponseDTO> criarComentario(
            @Valid @RequestBody ComentarioRequestDTO requestDTO,
            Authentication authentication) {

        // 1. Pegamos o utilizador autenticado a partir do token
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
        Long usuarioLogadoId = usuarioDetails.getUsuario().getId();
        requestDTO.setAutorId(usuarioLogadoId);

        // 2. Atualizamos o DTO com o ID do autor real para passar ao serviço
        // (Uma abordagem alternativa seria o serviço também aceitar o objeto Authentication)

        ComentarioResponseDTO novoComentario = comentarioService.criarComentario(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoComentario);
    }

    /**
     * Endpoint para listar todos os comentários de um post específico.
     * Este endpoint não precisa de saber quem é o utilizador logado, pois é uma ação de leitura pública.
     */
    @GetMapping("/posts/{postId}/comentarios")
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentariosDoPost(@PathVariable Long postId) {
        List<ComentarioResponseDTO> comentarios = comentarioService.listarComentariosPorPost(postId);
        return ResponseEntity.ok(comentarios);
    }
}