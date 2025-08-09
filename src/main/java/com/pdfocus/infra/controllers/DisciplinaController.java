package com.pdfocus.infra.controllers;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.*;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.infra.security.AuthenticationHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller REST para gerenciar as operações relacionadas às Disciplinas de um usuário.
 * Todos os endpoints são protegidos e operam no contexto do usuário autenticado.
 */
@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final ListarDisciplinasUseCase listarDisciplinasUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final ObterDisciplinaPorIdUseCase obterDisciplinaPorIdUseCase;
    private final DeletarDisciplinaUseCase deletarDisciplinaUseCase;
    private final AtualizarDisciplinaUseCase atualizarDisciplinaUseCase;
    private final AuthenticationHelper authenticationHelper;

    public DisciplinaController(
            ListarDisciplinasUseCase listarDisciplinasUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            ObterDisciplinaPorIdUseCase obterDisciplinaPorIdUseCase,
            DeletarDisciplinaUseCase deletarDisciplinaUseCase,
            AtualizarDisciplinaUseCase atualizarDisciplinaUseCase,
            AuthenticationHelper authenticationHelper) {
        this.listarDisciplinasUseCase = listarDisciplinasUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.obterDisciplinaPorIdUseCase = obterDisciplinaPorIdUseCase;
        this.deletarDisciplinaUseCase = deletarDisciplinaUseCase;
        this.atualizarDisciplinaUseCase = atualizarDisciplinaUseCase;
        this.authenticationHelper = authenticationHelper;
    }

    /**
     * Endpoint para listar todas as disciplinas do usuário autenticado.
     * Responde a requisições GET em /disciplinas.
     *
     * @return Uma lista de disciplinas pertencentes ao usuário logado.
     */
    @GetMapping
    public ResponseEntity<List<Disciplina>> listarPorUsuario() {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        List<Disciplina> disciplinas = listarDisciplinasUseCase.executar(usuarioId);
        return ResponseEntity.ok(disciplinas);
    }

    /**
     * Endpoint para criar uma nova disciplina para o usuário autenticado.
     * Responde a requisições POST em /disciplinas.
     *
     * @param command O comando com os dados para criar a disciplina.
     * @return Resposta 201 (Created) com a nova disciplina e a URL do recurso no cabeçalho 'Location'.
     */
    @PostMapping
    public ResponseEntity<Disciplina> criar(@RequestBody CriarDisciplinaCommand command) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Disciplina novaDisciplina = criarDisciplinaUseCase.executar(command, usuarioId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaDisciplina.getId())
                .toUri();

        return ResponseEntity.created(location).body(novaDisciplina);
    }

    /**
     * Endpoint para buscar uma única disciplina do usuário autenticado pelo seu ID.
     * Responde a requisições GET em /disciplinas/{id}.
     *
     * @param id O UUID da disciplina a ser buscada.
     * @return Resposta 200 (OK) com a disciplina se encontrada, ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Disciplina> obterPorId(@PathVariable UUID id) {
        // 1. Obtemos o ID do usuário logado a partir do token
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();

        // 2. Chamamos o método 'executar' com ambos os IDs (o da disciplina e o do usuário)
        Optional<Disciplina> disciplinaOptional = obterDisciplinaPorIdUseCase.executar(id, usuarioId);

        // O resto da lógica para retornar 200 OK ou 404 Not Found continua a mesma
        return disciplinaOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para atualizar uma disciplina existente do usuário autenticado.
     * Responde a requisições PUT em /disciplinas/{id}.
     *
     * @param id O UUID da disciplina a ser atualizada.
     * @param command O comando com os novos dados.
     * @return Resposta 200 (OK) com a disciplina atualizada, ou 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Disciplina> atualizar(@PathVariable UUID id, @RequestBody AtualizarDisciplinaCommand command) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        Optional<Disciplina> disciplinaAtualizadaOptional = atualizarDisciplinaUseCase.executar(id, command, usuarioId);
        return disciplinaAtualizadaOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para deletar uma disciplina existente do usuário autenticado.
     * Responde a requisições DELETE em /disciplinas/{id}.
     *
     * @param id O UUID da disciplina a ser deletada.
     * @return Resposta 204 (No Content) se a deleção for bem-sucedida, ou 404 (Not Found).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();
        deletarDisciplinaUseCase.executar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
