package com.pdfocus.infra.controllers;

import com.pdfocus.application.resumo.dto.AtualizarResumoCommand;
import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.application.resumo.port.entrada.*;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.infra.config.security.AuthenticationHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller REST para gerenciar as operações relacionadas aos Resumos de um usuário.
 * Todos os endpoints são protegidos e operam no contexto do usuário autenticado.
 */
@RestController
@RequestMapping("/resumos")
public class ResumoController {

    private final ListarResumosUseCase listarResumosUseCase;
    private final CriarResumoUseCase criarResumoUseCase;
    private final ObterResumoPorIdUseCase obterResumoPorIdUseCase;
    private final AtualizarResumoUseCase atualizarResumoUseCase;
    private final DeletarResumoUseCase deletarResumoUseCase;
    private final AuthenticationHelper authenticationHelper;
    private final GerarResumoAutomaticoUseCase gerarResumoAutomaticoUseCase;

    public ResumoController(
            ListarResumosUseCase listarResumosUseCase,
            CriarResumoUseCase criarResumoUseCase,
            ObterResumoPorIdUseCase obterResumoPorIdUseCase,
            AtualizarResumoUseCase atualizarResumoUseCase,
            DeletarResumoUseCase deletarResumoUseCase,
            GerarResumoAutomaticoUseCase gerarResumoAutomaticoUseCase,
            AuthenticationHelper authenticationHelper) {
        this.listarResumosUseCase = listarResumosUseCase;
        this.criarResumoUseCase = criarResumoUseCase;
        this.obterResumoPorIdUseCase = obterResumoPorIdUseCase;
        this.atualizarResumoUseCase = atualizarResumoUseCase;
        this.deletarResumoUseCase = deletarResumoUseCase;
        this.gerarResumoAutomaticoUseCase = gerarResumoAutomaticoUseCase;
        this.authenticationHelper = authenticationHelper;
    }

    /**
     * Endpoint para listar todos os resumos do usuário autenticado.
     * Responde a requisições GET em /resumos.
     *
     * @return Uma lista de resumos pertencentes ao usuário logado.
     */
    @GetMapping
    public ResponseEntity<List<Resumo>> listarPorUsuario() {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        List<Resumo> resumos = listarResumosUseCase.buscarTodosPorUsuario(usuarioId);
        return ResponseEntity.ok(resumos);
    }

    /**
     * Endpoint para criar um novo resumo para o usuário autenticado.
     * Responde a requisições POST em /resumos.
     *
     * @param command O comando com os dados para criar o resumo. O ID do usuário é obtido do token.
     * @return Resposta 201 (Created) com o novo resumo e a URL do recurso no cabeçalho 'Location'.
     */
    @PostMapping
    public ResponseEntity<Resumo> criar(@RequestBody CriarResumoCommand command) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Resumo novoResumo = criarResumoUseCase.executar(command, usuarioId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoResumo.getId())
                .toUri();

        return ResponseEntity.created(location).body(novoResumo);
    }

    /**
     * Endpoint para buscar um único resumo do usuário autenticado pelo seu ID.
     * Responde a requisições GET em /resumos/{id}.
     *
     * @param id O UUID do resumo a ser buscado.
     * @return Resposta 200 (OK) com o resumo se encontrado, ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resumo> obterPorId(@PathVariable UUID id) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Optional<Resumo> resumoOptional = obterResumoPorIdUseCase.executar(id, usuarioId);

        return resumoOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para atualizar um resumo existente do usuário autenticado.
     * Responde a requisições PUT em /resumos/{id}.
     *
     * @param id O UUID do resumo a ser atualizado.
     * @param command O comando com os novos dados (título e conteúdo).
     * @return Resposta 200 (OK) com o resumo atualizado, ou 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Resumo> atualizar(
            @PathVariable UUID id,
            @RequestBody AtualizarResumoCommand command) {

        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Optional<Resumo> resumoAtualizadoOptional = atualizarResumoUseCase.executar(id, usuarioId, command);

        return resumoAtualizadoOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para deletar um resumo existente do usuário autenticado.
     * Responde a requisições DELETE em /resumos/{id}.
     *
     * @param id O UUID do resumo a ser deletado.
     * @return Resposta 204 (No Content) se a deleção for bem-sucedida, ou 404 (Not Found).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        deletarResumoUseCase.executar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para gerar um resumo automaticamente a partir de um material (PDF) existente.
     * Responde a requisições POST em /resumos/gerar-automatico.
     *
     * @param command O comando com os dados do material e opções de geração.
     * @return Resposta 201 (Created) com o resumo gerado automaticamente.
     */
    @PostMapping("/gerar-automatico")
    public ResponseEntity<Resumo> gerarResumoAutomatico(@RequestBody CriarResumoDeMaterialCommand command) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Resumo resumoGerado = gerarResumoAutomaticoUseCase.executar(command, usuarioId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/../{id}") // Volta para o endpoint principal de resumos
                .buildAndExpand(resumoGerado.getId())
                .toUri();

        return ResponseEntity.created(location).body(resumoGerado);
    }
}

