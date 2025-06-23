package com.gatorion.backend.controller;


import com.gatorion.backend.dto.PostRequestDTO;
import com.gatorion.backend.dto.PostResponseDTO;
import com.gatorion.backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> listarPosts() {
        // Placeholder do usuário logado
        // No futuro, esse ID virá do token de autenticação do JWT
        Long usuarioLogadoId = 1L;
        // Long usuarioLogadoId = postRequestDTO.getIdAutor();

        List<PostResponseDTO> posts = postService.listarPosts(usuarioLogadoId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> criarPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {
        Long usuarioLogadoId = postRequestDTO.getIdAutor(); // Substitua pelo ID do usuário logado, se necessário
        // Long usuarioLogadoId = 1L; // Linha usada para testes no PostMan

        PostResponseDTO postCriado = postService.criarPost(postRequestDTO, usuarioLogadoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postCriado);
    }

    @PostMapping("/{postId}/curtir")
    public ResponseEntity<Void> curtirPost(@PathVariable Long postId, PostRequestDTO postRequestDTO) {
        // Nesse caso, estamos "chumbando" um valor para sumilar qual usuário vai curtir
        // Mas no futuro, implementaremos JWT, tornando o usuário dinâmico
        // Long usuarioLogadoId = postRequestDTO.getIdAutor(); // Substitua pelo ID do usuário logado, se necessário
        Long usuarioLogadoId = 1L;

        boolean curtidaNova = postService.curtirPost(postId, usuarioLogadoId);

        if (curtidaNova) {
            // A curtida foi criada agora, então retornamos 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            // A curtida já existia, então retornamos 200 OK
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/{postId}/curtir")
    public ResponseEntity<Void> descurtirPost(@PathVariable Long postId, PostRequestDTO postRequestDTO) {
        // Nesse caso, estamos "chumbando" um valor para sumilar qual usuário vai curtir
        // Mas no futuro, implementaremos JWT, tornando o usuário dinâmico
        Long usuarioLogadoId = 1L; // Nosso placeholder
        // Long usuarioLogadoId = postRequestDTO.getIdAutor();

        boolean curtidaDeletada = postService.descurtirPost(postId, usuarioLogadoId);

        if (curtidaDeletada) {
            // A curtida foi deletada agora, então retornamos 204 No Content, status HTTP ideal para delete bem-sucedido
            return ResponseEntity.noContent().build();
        } else {
            // A curtida não existia, então retornamos 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}
