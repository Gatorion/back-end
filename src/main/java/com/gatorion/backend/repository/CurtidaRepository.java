package com.gatorion.backend.repository;

import com.gatorion.backend.model.Curtida;
import com.gatorion.backend.model.Post;
import com.gatorion.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurtidaRepository extends JpaRepository<Curtida, Long> {

    // Encontra uma curtida específica por um usuário e um post.
    // Isso será usado para verificar se um usuário já curtiu um post antes.
    Optional<Curtida> findByUsuarioAndPost(Usuario usuario, Post post);

    // Busca as curtidas numa postagem
    @Query("SELECT curtd FROM Curtida curtd JOIN FETCH curtd.usuario WHERE curtd.post = :post")
    List<Curtida> findAllByPostWithUsuario(Post post);

    // Busca os posts que um usuário curtiu
    @Query("SELECT curtd FROM Curtida curtd JOIN FETCH curtd.post WHERE curtd.usuario = :usuario")
    List<Curtida> findAllByUsuarioWithPost(Usuario usuario);

    long countByPost(Post post);

}