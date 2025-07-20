package com.pdfocus.infra.controllers;

import com.pdfocus.application.resumo.dto.AtualizarResumoCommand;
import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.port.entrada.*;
import com.pdfocus.core.models.Resumo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller REST para gerenciar as operações relacionadas a Resumos.
 * Este controller será a porta de entrada HTTP para todas as funcionalidades de Resumo.
 */
@RestController
@RequestMapping("resumos")
public class ResumoController {

    private final ListarResumosUseCase listarResumosUseCase;
    private final CriarResumoUseCase criarResumoUseCase;
    private final ObterResumoPorIdUseCase obterResumoPorIdUseCase;
    private final AtualizarResumoUseCase atualizarResumoUseCase;
    private final DeletarResumoUseCase deletarResumoUseCase;

    public ResumoController(
            ListarResumosUseCase listarResumosUseCase,
            CriarResumoUseCase criarResumoUseCase,
            ObterResumoPorIdUseCase obterResumoPorIdUseCase,
            AtualizarResumoUseCase atualizarResumoUseCase,
            DeletarResumoUseCase deletarResumoUseCase) { // 2. Injetar no construtor
        this.listarResumosUseCase = listarResumosUseCase;
        this.criarResumoUseCase = criarResumoUseCase;
        this.obterResumoPorIdUseCase = obterResumoPorIdUseCase;
        this.atualizarResumoUseCase = atualizarResumoUseCase;
        this.deletarResumoUseCase = deletarResumoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Resumo>> listarPorUsuario(@RequestParam UUID usuarioId) {
        List<Resumo> resumos = listarResumosUseCase.buscarTodosPorUsuario(usuarioId);

        return ResponseEntity.ok(resumos);
    }

    /**
     * Endpoint para criar um novo resumo.
     * Responde a requisições POST em /resumos.
     * O corpo da requisição deve conter um JSON com os dados do resumo.
     *
     * @param command O comando com os dados para criar o resumo.
     * @return Uma resposta HTTP 201 (Created) com o resumo criado no corpo e a URL
     * do novo recurso no cabeçalho 'Location'.
     */
    @PostMapping
    public ResponseEntity<Resumo> criar(@RequestBody CriarResumoCommand command) {
        // Chama o caso de uso para criar o resumo
        Resumo novoResumo = criarResumoUseCase.executar(command);

        // Constrói a URI para o novo recurso
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoResumo.getId())
                .toUri();

        // Retorna a resposta 201 Created
        return ResponseEntity.created(location).body(novoResumo);
    }

    /**
     * Endpoint para buscar um único resumo pelo seu ID.
     * Requer também o ID do usuário para garantir a autorização.
     * Responde a requisições GET em /resumos/{id}?usuarioId={id-do-usuario}
     *
     * @param id O UUID do resumo a ser buscado, vindo da URL.
     * @param usuarioId O UUID do usuário proprietário, vindo do parâmetro de URL.
     * @return Resposta 200 (OK) com o resumo se encontrado,
     * ou 404 (Not Found) se não for encontrado ou não pertencer ao usuário.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resumo> obterPorId(@PathVariable UUID id, @RequestParam UUID usuarioId) {
        // Chama o caso de uso com ambos os IDs
        Optional<Resumo> resumoOptional = obterResumoPorIdUseCase.executar(id, usuarioId);

        // Retorna 200 OK com o corpo se encontrou, ou 404 Not Found se não encontrou
        return resumoOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para atualizar um resumo existente.
     * Responde a requisições PUT em /resumos/{id}?usuarioId={id-do-usuario}
     *
     * @param id O UUID do resumo a ser atualizado, vindo da URL.
     * @param usuarioId O UUID do usuário proprietário, para verificação.
     * @param command O comando com os novos dados para o resumo (título e conteúdo).
     * @return Resposta 200 (OK) com o resumo atualizado se encontrado,
     * ou 404 (Not Found) se o resumo não for encontrado ou não pertencer ao usuário.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Resumo> atualizar(
            @PathVariable UUID id,
            @RequestParam UUID usuarioId,
            @RequestBody AtualizarResumoCommand command) {

        // Chama o caso de uso para executar a atualização
        Optional<Resumo> resumoAtualizadoOptional = atualizarResumoUseCase.executar(id, usuarioId, command);

        // Retorna 200 OK com o corpo se atualizou, ou 404 Not Found se não encontrou
        return resumoAtualizadoOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para deletar um resumo existente.
     * Responde a requisições DELETE em /resumos/{id}?usuarioId={id-do-usuario}
     *
     * @param id O UUID do resumo a ser deletado, vindo da URL.
     * @param usuarioId O UUID do usuário proprietário, para verificação de segurança.
     * @return Resposta 204 (No Content) se a deleção for bem-sucedida,
     * ou 404 (Not Found) se o resumo não for encontrado ou não pertencer ao usuário.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id,
            @RequestParam UUID usuarioId) {

        // Apenas chama o caso de uso.
        // Se o resumo não for encontrado, o serviço/repositório lançará uma exceção.
        deletarResumoUseCase.executar(id, usuarioId);

        // Se o metodo chegar até aqui sem lançar uma exceção, a deleção foi bem-sucedida.
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
