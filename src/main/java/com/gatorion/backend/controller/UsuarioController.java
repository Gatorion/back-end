package com.gatorion.backend.controller;

import com.gatorion.backend.dto.*;
import com.gatorion.backend.model.Usuario;
import com.gatorion.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para gerenciar as operações relacionadas a Usuários.
 * Expõe endpoints para criar, ler, atualizar e deletar (CRUD) usuários.
 * Esta classe é a porta de entrada da API para as requisições HTTP de usuários.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para listar todos os usuários.
     * Mapeado para requisições GET em /usuarios/listar.
     *
     * @return ResponseEntity contendo uma lista de DTOs de resposta de usuário e status HTTP 200 (OK).
     * Retornar um DTO (Data Transfer Object) é uma boa prática para não expor a entidade do banco de dados.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        // 1. Busca a lista de entidades Usuario através do serviço.
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        // 2. Converte a lista de entidades Usuario para uma lista de UsuarioResponseDTO.
        //    Isso evita expor dados sensíveis, como a senha criptografada, na resposta da API.
        List<UsuarioResponseDTO> response = usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getNomeUsuario(), usuario.getXp(), usuario.getNivel()))
                .collect(Collectors.toList());

        // 3. Retorna a lista de DTOs num ResponseEntity com o status HTTP 200 (OK).
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para cadastrar um novo usuário.
     * Mapeado para requisições POST em /usuarios/cadastrar.
     *
     * @param dto O DTO (Data Transfer Object) contendo os dados do usuário a ser criado.
     *            A anotação @Valid aciona as validações definidas no UsuarioRequestDTO (ex: @NotBlank, @Email).
     * @return ResponseEntity contendo o DTO de resposta do novo usuário e status HTTP 201 (CREATED).
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody @Valid Usuario usuario) {
        try {
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Usuário cadastrado com sucesso!",
                "usuario", Map.of(
                    "id", usuarioSalvo.getId(),
                    "nome", usuarioSalvo.getNome(),
                    "email", usuarioSalvo.getEmail(),
                    "nomeUsuario", usuarioSalvo.getNomeUsuario()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao cadastrar usuário: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint para atualizar um usuário existente.
     * Mapeado para requisições PUT em /usuarios/{id}.
     *
     * @param id  O ID do usuário a ser atualizado, vindo da URL.
     * @param dto O DTO com os dados a serem atualizados.
     * @return ResponseEntity contendo o DTO de resposta do usuário atualizado e status HTTP 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO dto) { // Usar DTO na requisição é mais seguro e consistente.

        // 1. Cria um objeto Usuario com os dados recebidos para passar ao serviço.
        Usuario dadosParaAtualizar = new Usuario();
        dadosParaAtualizar.setNome(dto.getNome());
        dadosParaAtualizar.setEmail(dto.getEmail());
        dadosParaAtualizar.setSenha(dto.getSenha());

        // 2. Chama o serviço para realizar a atualização. O serviço cuidará de encontrar o usuário e salvar.
        //    Isso centraliza a lógica de negócio no serviço, mantendo o controlador limpo.
        Usuario usuario = usuarioService.atualizarUsuario(id, dadosParaAtualizar);

        // 3. Cria um DTO de resposta para enviar de volta ao cliente.
        UsuarioResponseDTO response = new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getNomeUsuario(), usuario.getXp(), usuario.getNivel());

        // 4. Retorna o DTO com os dados atualizados e status 200 (OK).
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para buscar um usuário específico pelo seu ID.
     * Mapeado para requisições GET em /usuarios/{id}.
     *
     * @param id O ID do usuário a ser buscado.
     * @return ResponseEntity contendo o DTO de resposta do usuário e status HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        // 1. Busca o usuário pelo serviço. O serviço lançará uma exceção se não encontrar.
        Usuario usuario = usuarioService.buscarPorId(id);

        // 2. Converte a entidade para um DTO de resposta para não expor a senha.
        UsuarioResponseDTO response = new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getNomeUsuario(), usuario.getXp(), usuario.getNivel());

        // 3. Retorna o DTO com status 200 (OK).
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para deletar um usuário pelo seu ID.
     * Mapeado para requisições DELETE em /usuarios/{id}.
     *
     * @param id O ID do usuário a ser deletado.
     * @return ResponseEntity com uma mensagem de sucesso e condição HTTP 200 (OK), ou um status de erro apropriado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        try {
            // 1. Tenta deletar o usuário através do serviço.
            usuarioService.deletarUsuario(id);
            // 2. Se a exclusão for bem-sucedida, retorna uma resposta de sucesso com uma mensagem clara.
            return ResponseEntity.ok("Usuário deletado com sucesso!");
        } catch (RuntimeException erro) {
            // 3. Captura a exceção lançada pelo serviço se o usuário não for encontrado.
            //    O ideal é criar uma exceção customizada (ex: RecursoNaoEncontradoException).
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());
        }
    }

    @PostMapping("/{id}/add-xp")
    public ResponseEntity<UsuarioResponseDTO> adicionarXp(
            @PathVariable Long id,
            @RequestBody XpRequestDTO xpRequest) { // Usamos um DTO para receber o XP

        Usuario usuarioAtualizado = usuarioService.adicionarXp(id, xpRequest.getXp());

        // Criamos uma resposta DTO para enviar ao frontend
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getNomeUsuario(),
                usuarioAtualizado.getXp(),
                usuarioAtualizado.getNivel()
        );

        return ResponseEntity.ok(response);
    }                   //quem vai ser seguido / seguir / seguidor
    @PostMapping("/{nomeInfluencer}/seguir/{nomeSeguidor}")
    public ResponseEntity<SeguidorResponseDTO> seguirOuDeixarDeSeguir(
            @PathVariable String nomeInfluencer,
            @PathVariable String nomeSeguidor) {
        SeguidorRequestDTO requisicao = new SeguidorRequestDTO(nomeInfluencer, nomeSeguidor);

        SeguidorResponseDTO nomesDosSeguidores = usuarioService.adicionarSeguidor(requisicao.getInfluecer(), requisicao.getSeguidor());
        return ResponseEntity.ok(nomesDosSeguidores);
    }
}