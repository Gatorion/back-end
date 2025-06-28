package com.gatorion.backend.controller;

import com.gatorion.backend.dto.ComentarioRequestDTO;
import com.gatorion.backend.dto.ComentarioResponseDTO;
import com.gatorion.backend.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Usando um prefixo /api para padronizar
@CrossOrigin(origins = "*")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @Autowired
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    /**
     * Endpoint para criar um novo comentário em um post.
     */
    @PostMapping("/comentarios")
    public ResponseEntity<ComentarioResponseDTO> criarComentario(@Valid @RequestBody ComentarioRequestDTO requestDTO) {
        ComentarioResponseDTO novoComentario = comentarioService.criarComentario(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoComentario);
    }

    /**
     * Endpoint para listar todos os comentários de um post específico.
     */
    @GetMapping("/posts/{postId}/comentarios")
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentariosDoPost(@PathVariable Long postId) {
        List<ComentarioResponseDTO> comentarios = comentarioService.listarComentariosPorPost(postId);
        return ResponseEntity.ok(comentarios);
    }
}