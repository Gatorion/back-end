package com.gatorion.backend.service;

import com.gatorion.backend.dto.PostRequestDTO;
import com.gatorion.backend.dto.PostResponseDTO;
import com.gatorion.backend.model.Curtida;
import com.gatorion.backend.model.Post;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.PostRepository;
import com.gatorion.backend.repository.UsuarioRepository;
import com.gatorion.backend.repository.CurtidaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;
    private final CurtidaRepository curtidaRepository;

    @Autowired
    public PostService(PostRepository postRepository, UsuarioRepository usuarioRepository, CurtidaRepository curtidaRepository) {
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
        this.curtidaRepository = curtidaRepository;
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
                postSalvo.getAutor().getNome(),
                0L, // Total de curtidas para um post novo é sempre 0
                false // O autor não curtiu o post automaticamente
        );
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> listarPosts(Long usuarioLogadoId) {
        List<Post> posts = postRepository.findAllByOrderByDataCriacaoDesc();

        // Se o usuário não estiver logad (ID nulo), não podemos checar as curtidas que ele tem
        // Criamos um objeto Usuario temporário para evitar erros, ou podemos passar nulo
        // Por enquanto, como focamos no MVP, vamos assumir que ele sempre vai estar logado
        Usuario usuarioLogado = usuarioRepository.findById(usuarioLogadoId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return posts.stream()
                .map(post -> {
                    // 2. Para cada post, calculamos os novos dados
                    long contagemCurtidas = curtidaRepository.countByPost(post);
                    boolean usuarioCurtiu = curtidaRepository.findByUsuarioAndPost(usuarioLogado, post).isPresent();

                    // 3. Criamos o DTO com os novos campos
                    return new PostResponseDTO(
                            post.getId(),
                            post.getConteudo(),
                            post.getDataCriacao(),
                            post.getAutor().getId(),
                            post.getAutor().getNome(),
                            contagemCurtidas,
                            usuarioCurtiu
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

