package com.gatorion.backend.service;

import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.gatorion.backend.dto.SeguidorResponseDTO;

import java.util.stream.Collectors;

/**
 * Classe de serviço para gerenciar as operações de negócio relacionadas a Usuários.
 * Contém a lógica para criar, ler, atualizar e deletar (CRUD) usuários.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Construtor para injeção de dependência do UsuarioRepository e BCryptPasswordEncoder.
     *
     * @param usuarioRepository O repositório para operações de dados de usuário.
     * @param passwordEncoder   O codificador para senhas.
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Salva um novo usuário no banco de dados após validar e criptografar a senha.
     *
     * @param usuario O objeto Usuario a ser salvo.
     * @return O objeto Usuario salvo com o ID gerado.
     * @throws RuntimeException se o e-mail fornecido já estiver cadastrado.
     */
    public Usuario salvarUsuario(Usuario usuario) {
        // 1. Valida se o e-mail já está em uso para evitar duplicidade.
        usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email já cadastrado");
        });

        // 2. Criptografa a senha do usuário antes de persistir no banco.
        // Antes, a senha estava sendo criptografada ANTES da validação no passo 1,
        // e tornava o processamento inútil caso a validação retornasse true (já existe o email).
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // 3. Salva o novo usuário no banco de dados.
        return usuarioRepository.save(usuario);
    }

    /**
     * Valida se uma senha fornecida (em texto plano) corresponde a uma senha criptografada.
     *
     * @param senha              A senha em texto plano a ser verificada.
     * @param senhaCriptografada A senha criptografada armazenada no banco.
     * @return true se as senhas corresponderem, false caso contrário.
     */
    public boolean validarSenha(String senha, String senhaCriptografada) {
        return passwordEncoder.matches(senha, senhaCriptografada);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id                 O ID do usuário a ser atualizado.
     * @param dadosParaAtualizar Um objeto Usuario contendo os novos dados.
     * @return O usuário com os dados atualizados.
     * @throws RuntimeException se o usuário não for encontrado ou se o novo e-mail já pertencer a outro usuário.
     */
    public Usuario atualizarUsuario(Long id, Usuario dadosParaAtualizar) {
        // 1. Busca o usuário existente pelo ID. Lança uma exceção se não for encontrado.
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Valida se o e-mail foi alterado e, em caso afirmativo, se o novo e-mail já está em uso por outro usuário.
        if (!usuario.getEmail().equals(dadosParaAtualizar.getEmail())) {
            usuarioRepository.findByEmail(dadosParaAtualizar.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    throw new RuntimeException("O novo email já está cadastrado para outro usuário.");
                }
            });
        }

        // 3. Atualiza os campos do usuário com os novos dados.
        usuario.setNome(dadosParaAtualizar.getNome());
        usuario.setNomeUsuario(dadosParaAtualizar.getNomeUsuario());
        usuario.setEmail(dadosParaAtualizar.getEmail());

        // 4. **IMPLEMENTAÇÃO PRINCIPAL**: Atualiza a senha somente se uma nova senha for fornecida.
        //    Isso evita que a senha seja apagada acidentalmente durante a atualização de outros campos.
        if (dadosParaAtualizar.getSenha() != null && !dadosParaAtualizar.getSenha().trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(dadosParaAtualizar.getSenha()));
        }

        // 5. Salva as alterações no banco de dados.
        return usuarioRepository.save(usuario);
    }

    /**
     * Retorna uma lista com todos os usuários cadastrados.
     *
     * @return Uma lista de objetos Usuario.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca e retorna um usuário específico pelo seu ID.
     *
     * @param id O ID do usuário a ser buscado.
     * @return O objeto Usuario correspondente ao ID.
     * @throws RuntimeException se nenhum usuário for encontrado com o ID fornecido.
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    /**
     * Deleta um usuário do banco de dados com base no seu ID.
     *
     * @param id O ID do usuário a ser deletado.
     * @throws RuntimeException se nenhum usuário for encontrado com o ID fornecido.
     */
    public void deletarUsuario(Long id) {
        // Verifica se o usuário existe antes de tentar deletar para lançar uma exceção clara.
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public SeguidorResponseDTO adicionarSeguidor(String nomeInfluencer, String nomeSeguidor) { // pegar pelo nome no bd

        if(nomeInfluencer.equals(nomeSeguidor)) throw new IllegalArgumentException("Um usuário não pode se seguir");
        //pelo nome a gente encontra o seguidor e quem está sendo seguido
        Usuario influencer = usuarioRepository.findByNomeUsuario(nomeInfluencer)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Usuario seguidor = usuarioRepository.findByNomeUsuario(nomeSeguidor)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean aindaNaoSegue = !influencer.getSeguidores().contains(seguidor);

        List<Usuario> seguidores = influencer.getSeguidores();//quem segue ele
        List<Usuario> seguindo = influencer.getSeguindo();//ele segue

        if(aindaNaoSegue) {
            seguidores.add(seguidor);//começa a seguir
        } else {
            seguidores.remove(seguidor);//deixa de seguir
        }
        usuarioRepository.save(influencer);

        SeguidorResponseDTO seguidorResponseDTO = new SeguidorResponseDTO();
        seguidorResponseDTO.setSeguidores( seguidores.stream()
                .map(Usuario::getNomeUsuario)
                .collect(Collectors.toList()));

        seguidorResponseDTO.setSeguindo( seguindo.stream()
                .map(Usuario::getNomeUsuario)
                .collect(Collectors.toList()));

        return seguidorResponseDTO;
    }
}