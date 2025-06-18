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

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> listarPosts() {
        List<PostResponseDTO> posts = postService.listarPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> criarPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {
        Long usuarioLogadoId = postRequestDTO.getIdAutor(); // Substitua pelo ID do usuário logado, se necessário

        PostResponseDTO postCriado = postService.criarPost(postRequestDTO, usuarioLogadoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postCriado);
    }
}
