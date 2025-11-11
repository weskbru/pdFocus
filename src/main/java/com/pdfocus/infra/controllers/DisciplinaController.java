package com.pdfocus.infra.controllers;

import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;
import com.pdfocus.application.disciplina.dto.DisciplinaResponse;
import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.*;
import com.pdfocus.core.models.Disciplina;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador REST responsável pelo gerenciamento de {@link Disciplina}.
 * <p>
 * Todos os endpoints operam no contexto do usuário autenticado e seguem os princípios REST:
 * - Representação por recursos;
 * - Retorno adequado de status HTTP;
 * - Lógica de domínio delegada à camada de aplicação (casos de uso).
 * </p>
 */
@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final ListarDisciplinasUseCase listarDisciplinasUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final ObterDisciplinaPorIdUseCase obterDisciplinaPorIdUseCase;
    private final DeletarDisciplinaUseCase deletarDisciplinaUseCase;
    private final AtualizarDisciplinaUseCase atualizarDisciplinaUseCase;

    public DisciplinaController(
            ListarDisciplinasUseCase listarDisciplinasUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            ObterDisciplinaPorIdUseCase obterDisciplinaPorIdUseCase,
            DeletarDisciplinaUseCase deletarDisciplinaUseCase,
            AtualizarDisciplinaUseCase atualizarDisciplinaUseCase
    ) {
        this.listarDisciplinasUseCase = listarDisciplinasUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.obterDisciplinaPorIdUseCase = obterDisciplinaPorIdUseCase;
        this.deletarDisciplinaUseCase = deletarDisciplinaUseCase;
        this.atualizarDisciplinaUseCase = atualizarDisciplinaUseCase;
    }

    /**
     * Retorna todas as disciplinas pertencentes ao usuário autenticado.
     *
     * @return 200 (OK) com uma lista de {@link DisciplinaResponse}.
     */
    @GetMapping
    public ResponseEntity<List<DisciplinaResponse>> listarDisciplinas() {
        List<Disciplina> disciplinas = listarDisciplinasUseCase.executar();

        List<DisciplinaResponse> response = disciplinas.stream()
                .map(DisciplinaResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Cria uma nova disciplina associada ao usuário autenticado.
     *
     * @param command Dados necessários para criar a disciplina.
     * @return 201 (Created) com o recurso criado no corpo e no cabeçalho Location.
     */
    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@RequestBody CriarDisciplinaCommand command) {
        Disciplina novaDisciplina = criarDisciplinaUseCase.executar(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaDisciplina.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(DisciplinaResponse.fromDomain(novaDisciplina));
    }

    /**
     * Busca uma disciplina específica do usuário autenticado pelo seu ID.
     *
     * @param id Identificador único da disciplina.
     * @param pageable Informações de paginação (usadas para listar materiais/resumos associados).
     * @return 200 (OK) com {@link DetalheDisciplinaResponse} se encontrada, ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetalheDisciplinaResponse> obterPorId(
            @PathVariable UUID id,
            Pageable pageable
    ) {
        return obterDisciplinaPorIdUseCase.executar(id, pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Atualiza uma disciplina existente.
     *
     * @param id Identificador único da disciplina.
     * @param command Dados para atualização.
     * @return 200 (OK) com {@link DisciplinaResponse} atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> atualizar(
            @PathVariable UUID id,
            @RequestBody AtualizarDisciplinaCommand command
    ) {
        Disciplina disciplinaAtualizada = atualizarDisciplinaUseCase.executar(id, command);
        return ResponseEntity.ok(DisciplinaResponse.fromDomain(disciplinaAtualizada));
    }

    /**
     * Remove uma disciplina do usuário autenticado.
     *
     * @param id Identificador único da disciplina a ser removida.
     * @return 204 (No Content) se a deleção for bem-sucedida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        deletarDisciplinaUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
