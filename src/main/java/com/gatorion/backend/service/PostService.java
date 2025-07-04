package com.gatorion.backend.service;

import com.gatorion.backend.dto.PostRequestDTO;
import com.gatorion.backend.dto.PostResponseDTO;
import com.gatorion.backend.model.Curtida;
import com.gatorion.backend.model.Post;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.ComentarioRepository;
import com.gatorion.backend.repository.CurtidaRepository;
import com.gatorion.backend.repository.PostRepository;
import com.gatorion.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;
    private final CurtidaRepository curtidaRepository;
    private final ComentarioRepository comentarioRepository;


    @Autowired
    public PostService(PostRepository postRepository, UsuarioRepository usuarioRepository, CurtidaRepository curtidaRepository, ComentarioRepository comentarioRepository) {
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
        this.curtidaRepository = curtidaRepository;
        this.comentarioRepository = comentarioRepository;
    }

    @Transactional
    public PostResponseDTO criarPost(PostRequestDTO postRequestDTO, Long idAutor) {
        Usuario autor = usuarioRepository.findById(idAutor)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + idAutor + " não encontrado"));

        Post novoPost = new Post();
        novoPost.setConteudo(postRequestDTO.getConteudo());
        novoPost.setAutor(autor);

        Post postSalvo = postRepository.save(novoPost);

        return new PostResponseDTO(
                postSalvo.getId(),
                postSalvo.getConteudo(),
                postSalvo.getDataCriacao(),
                postSalvo.getAutor().getId(),
                postSalvo.getAutor().getNomeUsuario(),
                0L,      // Total de curtidas para um post novo é 0
                false,   // O autor não curtiu o post automaticamente
                0L       // Total de comentários para um post novo é sempre 0
        );
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> listarPosts(Long usuarioLogadoId) { // O nome do seu método pode ser listarTodos

        List<Post> posts = postRepository.findAllByOrderByDataCriacaoDesc();
        Usuario usuarioLogado = usuarioRepository.findById(usuarioLogadoId)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

        return posts.stream()
                .map(post -> {
                    long contagemCurtidas = curtidaRepository.countByPost(post);
                    boolean usuarioCurtiu = curtidaRepository.findByUsuarioAndPost(usuarioLogado, post).isPresent();

                    // 1. Para cada post, calculamos o total de comentários
                    long contagemComentarios = comentarioRepository.countByPost(post);

                    // 2. Criamos o DTO com o novo campo
                    return new PostResponseDTO(
                            post.getId(),
                            post.getConteudo(),
                            post.getDataCriacao(),
                            post.getAutor().getId(),
                            post.getAutor().getNomeUsuario(),
                            contagemCurtidas,
                            usuarioCurtiu,
                            contagemComentarios // Passando o total de comentários
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean curtirPost(Long postId, Long usuarioId) { // Mudança: agora retorna boolean
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado com o ID: " + postId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

        System.out.println(usuarioId);

        // Se a curtida já estiver presente, retorna 'false'
        if (curtidaRepository.findByUsuarioAndPost(usuario, post).isPresent()) {
            return false;
        }

        // Se não, cria a curtida e retorna 'true'
        Curtida novaCurtida = new Curtida(usuario, post);
        curtidaRepository.save(novaCurtida);
        return true;
    }

    @Transactional
    public boolean descurtirPost(Long postId, Long usuarioId) { // Mudança: agora retorna boolean
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado com o ID: " + postId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

        // Busca pela curtida
        Optional<Curtida> curtidaOptional = curtidaRepository.findByUsuarioAndPost(usuario, post);

        if (curtidaOptional.isPresent()) {
            // Se a curtida existe, deleta e retorna 'true'
            curtidaRepository.delete(curtidaOptional.get());
            return true;
        } else {
            // Se a curtida não existe, não faz nada e retorna 'false'
            return false;
        }
    }
}

