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
 * Controlador REST responsável por operações com Resumos.
 * <p>
 * Este controlador implementa as rotas que permitem ao usuário autenticado
 * criar, listar, atualizar, deletar e gerar automaticamente resumos a partir
 * de materiais (como PDFs). Toda a lógica de negócio é delegada para os
 * casos de uso da camada de aplicação.
 * </p>
 */
@RestController
@RequestMapping("/resumos")
public class ResumoController {

    private final ListarResumosUseCase listarResumosUseCase;
    private final CriarResumoUseCase criarResumoUseCase;
    private final ObterResumoPorIdUseCase obterResumoPorIdUseCase;
    private final AtualizarResumoUseCase atualizarResumoUseCase;
    private final DeletarResumoUseCase deletarResumoUseCase;
    private final GerarResumoAutomaticoUseCase gerarResumoAutomaticoUseCase;
    private final AuthenticationHelper authenticationHelper;

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
     * Lista todos os resumos pertencentes ao usuário autenticado.
     *
     * @return 200 (OK) com lista de {@link Resumo} do usuário.
     */
    @GetMapping
    public ResponseEntity<List<Resumo>> listarPorUsuario() {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        List<Resumo> resumos = listarResumosUseCase.buscarTodosPorUsuario(usuarioId);
        return ResponseEntity.ok(resumos);
    }

    /**
     * Cria um novo resumo para o usuário autenticado.
     * <p>
     * O ID do usuário é obtido automaticamente do token de autenticação.
     * </p>
     *
     * @param command DTO contendo título, conteúdo e disciplina do resumo.
     * @return 201 (Created) com o {@link Resumo} criado e a URI do recurso no cabeçalho Location.
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
     * Busca um resumo específico pelo seu ID, garantindo que ele pertença
     * ao usuário autenticado.
     *
     * @param id UUID do resumo a ser buscado.
     * @return 200 (OK) com o resumo encontrado ou 404 (Not Found) se não existir.
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
     * Atualiza o conteúdo de um resumo existente.
     * <p>
     * O usuário precisa ser o proprietário do resumo.
     * </p>
     *
     * @param id UUID do resumo a ser atualizado.
     * @param command DTO contendo novo título e conteúdo.
     * @return 200 (OK) com o resumo atualizado, ou 404 (Not Found) se não pertencer ao usuário.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Resumo> atualizar(
            @PathVariable UUID id,
            @RequestBody AtualizarResumoCommand command) {

        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Optional<Resumo> resumoAtualizado = atualizarResumoUseCase.executar(id, usuarioId, command);

        return resumoAtualizado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Exclui um resumo pertencente ao usuário autenticado.
     *
     * @param id UUID do resumo a ser removido.
     * @return 204 (No Content) se a exclusão for bem-sucedida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        deletarResumoUseCase.executar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Gera automaticamente um resumo a partir de um material (ex: PDF).
     * <p>
     * O processo envolve a extração de texto, processamento de IA e persistência
     * do resumo associado ao usuário autenticado.
     * </p>
     *
     * @param command DTO com dados do material e parâmetros de geração.
     * @return 201 (Created) com o resumo gerado automaticamente.
     */
    @PostMapping("/gerar-automatico")
    public ResponseEntity<Resumo> gerarResumoAutomatico(@RequestBody CriarResumoDeMaterialCommand command) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Resumo resumoGerado = gerarResumoAutomaticoUseCase.executar(command, usuarioId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/resumos/{id}")
                .buildAndExpand(resumoGerado.getId())
                .toUri();

        return ResponseEntity.created(location).body(resumoGerado);
    }
}
