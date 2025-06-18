package com.gatorion.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gatorion.backend.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}