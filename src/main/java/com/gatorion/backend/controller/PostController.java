package com.gatorion.backend.controller;

import com.gatorion.backend.dto.PostRequestDTO;
import com.gatorion.backend.dto.PostResponseDTO;
import com.gatorion.backend.security.UsuarioDetailsImpl;
import com.gatorion.backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
// A anotação @CrossOrigin agora é desnecessária, pois já temos uma configuração global no SecurityConfig
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // --- MÉTODOS ATUALIZADOS COM SEGURANÇA REAL ---

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> listarPosts(Authentication authentication) {
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
        Long usuarioLogadoId = usuarioDetails.getUsuario().getId(); // Pegamos o ID do token!

        List<PostResponseDTO> posts = postService.listarPosts(usuarioLogadoId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> criarPost(@Valid @RequestBody PostRequestDTO postRequestDTO, Authentication authentication) {
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
        Long usuarioLogadoId = usuarioDetails.getUsuario().getId(); // Pegamos o ID do token!

        // O autorId do DTO não é mais usado, garantindo que o utilizador só pode postar por si.
        PostResponseDTO postCriado = postService.criarPost(postRequestDTO, usuarioLogadoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postCriado);
    }

    @PostMapping("/{postId}/curtir")
    public ResponseEntity<Void> curtirPost(@PathVariable Long postId, Authentication authentication) {
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
        Long usuarioLogadoId = usuarioDetails.getUsuario().getId(); // Pegamos o ID do token!

        boolean curtidaNova = postService.curtirPost(postId, usuarioLogadoId);

        if (curtidaNova) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/{postId}/curtir")
    public ResponseEntity<Void> descurtirPost(@PathVariable Long postId, Authentication authentication) {
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
        Long usuarioLogadoId = usuarioDetails.getUsuario().getId(); // Pegamos o ID do token!

        boolean curtidaDeletada = postService.descurtirPost(postId, usuarioLogadoId);

        if (curtidaDeletada) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}