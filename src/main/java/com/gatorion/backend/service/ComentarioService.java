package com.gatorion.backend.service;

import com.gatorion.backend.dto.ComentarioRequestDTO;
import com.gatorion.backend.dto.ComentarioResponseDTO;
import com.gatorion.backend.model.Comentario;
import com.gatorion.backend.model.Post;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.ComentarioRepository;
import com.gatorion.backend.repository.PostRepository;
import com.gatorion.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository, PostRepository postRepository, UsuarioRepository usuarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cria um novo comentário em um post.
     */
    @Transactional
    public ComentarioResponseDTO criarComentario(ComentarioRequestDTO requestDTO) {
        // 1. Busca as entidades principais (Post e Usuario)
        Post post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post não encontrado com o ID: " + requestDTO.getPostId()));

        Usuario autor = usuarioRepository.findById(requestDTO.getAutorId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + requestDTO.getAutorId()));

        // 2. Cria a nova entidade Comentario
        Comentario novoComentario = new Comentario();
        novoComentario.setConteudo(requestDTO.getConteudo());
        novoComentario.setAutor(autor);
        novoComentario.setPost(post);
        // A data de criação será definida automaticamente pela anotação @PrePersist

        // 3. Salva o comentário no banco de dados
        Comentario comentarioSalvo = comentarioRepository.save(novoComentario);

        // 4. Mapeia para o DTO de resposta e retorna
        return new ComentarioResponseDTO(
                comentarioSalvo.getId(),
                comentarioSalvo.getConteudo(),
                comentarioSalvo.getDataCriacao(),
                comentarioSalvo.getAutor().getId(),
                comentarioSalvo.getAutor().getNome(),
                comentarioSalvo.getAutor().getNomeUsuario() // Usando o nome de usuário!
        );
    }

    /**
     * Lista todos os comentários de um post específico.
     */
    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> listarComentariosPorPost(Long postId) {
        // 1. Busca a entidade Post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado com o ID: " + postId));

        // 2. Usa nosso método otimizado para buscar os comentários já com os autores
        List<Comentario> comentarios = comentarioRepository.findAllByPostWithAutor(post);

        // 3. Mapeia a lista de entidades para uma lista de DTOs
        return comentarios.stream()
                .map(comentario -> new ComentarioResponseDTO(
                        comentario.getId(),
                        comentario.getConteudo(),
                        comentario.getDataCriacao(),
                        comentario.getAutor().getId(),
                        comentario.getAutor().getNome(),
                        comentario.getAutor().getNomeUsuario()
                ))
                .collect(Collectors.toList());
    }
}