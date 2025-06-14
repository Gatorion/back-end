package com.gatorion.backend.service;

import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvarUsuario(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senhaCriptografada);
        // Verifica se o email já está cadastrado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        // Verifica se o nome já está cadastrado
        return usuarioRepository.save(usuario);
    }

    public boolean validarSenha(String senha, String senhaCriptografada) {
        return passwordEncoder.matches(senha, senhaCriptografada);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        // Adicione outros campos se houver

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }
}
