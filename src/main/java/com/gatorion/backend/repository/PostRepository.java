package com.gatorion.backend.repository;

import com.gatorion.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByDataCriacaoDesc(); // ← Mudado de DataPostagem
}
