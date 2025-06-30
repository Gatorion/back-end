package com.gatorion.backend.repository;

import com.gatorion.backend.model.Comentario;
import com.gatorion.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Encontra todos os comentários de um post específico, trazendo os dados do autor junto.
    // Ordena do mais novo para o mais antigo.
    @Query("SELECT c FROM Comentario c JOIN FETCH c.autor WHERE c.post = :post ORDER BY c.dataCriacao DESC")
    List<Comentario> findAllByPostWithAutor(Post post);

    // Conta quantas entradas na tabela 'comentario' existem para um determinado Post.
    // Aqui o Spring Data JPA cria a query 'SELECT COUNT(*)...' por baixo dos panos.
    long countByPost(Post post);

}