package com.gatorion.backend.repository;

import com.gatorion.backend.model.Comentario;
import com.gatorion.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query("SELECT c FROM Comentario c JOIN FETCH c.autor WHERE c.post = :post ORDER BY c.dataCriacao DESC") // ← Mudado
    List<Comentario> findAllByPostWithAutor(Post post);

    long countByPost(Post post);
}