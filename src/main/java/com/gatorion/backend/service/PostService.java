package com.gatorion.backend.service;

import com.gatorion.backend.dto.PostRequestDTO;
import com.gatorion.backend.dto.PostResponseDTO;
import com.gatorion.backend.model.Post;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.PostRepository;
import com.gatorion.backend.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PostService(PostRepository postRepository, UsuarioRepository usuarioRepository) {
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PostResponseDTO criarPost(PostRequestDTO postRequestDTO, Long idAutor) {
        Usuario autor = usuarioRepository.findById(idAutor)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + idAutor + "não encontrado"));

        Post novoPost = new Post();
        novoPost.setConteudo(postRequestDTO.getConteudo());
        novoPost.setAutor(autor);

        Post postSalvo = postRepository.save(novoPost);

        return new PostResponseDTO(
                postSalvo.getId(),
                postSalvo.getConteudo(),
                postSalvo.getDataCriacao(),
                postSalvo.getAutor().getId(),
                postSalvo.getAutor().getNome()
        );
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> listarPosts() {
        List<Post> posts = postRepository.findAllByOrderByDataCriacaoDesc();

        return posts.stream()
                .map(post -> new PostResponseDTO(
                                post.getId(),
                                post.getConteudo(),
                                post.getDataCriacao(),
                                post.getAutor().getId(),
                                post.getAutor().getNome()


                        )
                )
                .collect(Collectors.toList());

    }
}

