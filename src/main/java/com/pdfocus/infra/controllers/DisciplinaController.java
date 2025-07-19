package com.pdfocus.infra.controllers;

// Novos imports

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.*;
import com.pdfocus.core.models.Disciplina;

// Novos imports
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Se precisar de URI para o cabeçalho Location
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final ListarDisciplinasUseCase listarDisciplinasUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final ObterDisciplinaPorIdUseCase obterDisciplinaPorIdUseCase;
    private final DeletarDisciplinaUseCase deletarDisciplinaUseCase;
    private final AtualizarDisciplinaUseCase atualizarDisciplinaUseCase; // 1. Adicionar como dependência

    public DisciplinaController(
            ListarDisciplinasUseCase listarDisciplinasUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            ObterDisciplinaPorIdUseCase obterDisciplinaPorIdUseCase,
            DeletarDisciplinaUseCase deletarDisciplinaUseCase,
            AtualizarDisciplinaUseCase atualizarDisciplinaUseCase) { // 2. Injetar no construtor
        this.listarDisciplinasUseCase = listarDisciplinasUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.obterDisciplinaPorIdUseCase = obterDisciplinaPorIdUseCase;
        this.deletarDisciplinaUseCase = deletarDisciplinaUseCase;
        this.atualizarDisciplinaUseCase = atualizarDisciplinaUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Disciplina>> listarTodas() {
        List<Disciplina> disciplinas = listarDisciplinasUseCase.listarTodas();
        return ResponseEntity.ok(disciplinas);
    }

    /**
     * Endpoint para buscar uma única disciplina pelo seu ID.
     * Responde a requisições GET em /disciplinas/{id}.
     *
     * @param id O UUID da disciplina a ser buscada, vindo da URL.
     * @return Resposta 200 (OK) com a disciplina se encontrada,
     * ou 404 (Not Found) se não for encontrada.
     */
    @GetMapping("/{id}") // 4. Novo endpoint para GET com um ID na URL
    public ResponseEntity<Disciplina> obterPorId(@PathVariable UUID id) {
        // 5. Chama o caso de uso para buscar a disciplina
        Optional<Disciplina> disciplinaOptional = obterDisciplinaPorIdUseCase.executar(id);

        // 6. Retorna 200 OK com o corpo se encontrou, ou 404 Not Found se não encontrou
        return disciplinaOptional
                .map(ResponseEntity::ok) // Se o Optional contém um valor, cria um ResponseEntity.ok(valor)
                .orElseGet(() -> ResponseEntity.notFound().build()); // Se o Optional estiver vazio, cria um ResponseEntity 404
    }

    @PostMapping
    public ResponseEntity<Disciplina> criar(@RequestBody CriarDisciplinaCommand command) {
        Disciplina novaDisciplina = criarDisciplinaUseCase.criar(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaDisciplina.getId())
                .toUri();

        return ResponseEntity.created(location).body(novaDisciplina);
    }

    /**
     * Endpoint para deletar uma disciplina pelo seu ID.
     * Responde a requisições DELETE em /disciplinas/{id}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        // 3. Chama o caso de uso para executar a deleção
        deletarDisciplinaUseCase.executar(id);

        // Retorna 204 No Content para indicar sucesso na operação
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para atualizar uma disciplina existente.
     * Responde a requisições PUT em /disciplinas/{id}.
     * O corpo da requisição deve conter um JSON com os novos "nome" e "descricao".
     *
     * @param id O UUID da disciplina a ser atualizada, vindo da URL.
     * @param command O comando com os novos dados para a disciplina.
     * @return Resposta 200 (OK) com a disciplina atualizada se encontrada,
     * ou 404 (Not Found) se a disciplina não for encontrada.
     */
    @PutMapping("/{id}") // 3. Novo endpoint para PUT
    public ResponseEntity<Disciplina> atualizar(@PathVariable UUID id, @RequestBody AtualizarDisciplinaCommand command) {
        // 4. Chama o caso de uso para atualizar
        Optional<Disciplina> disciplinaAtualizadaOptional = atualizarDisciplinaUseCase.executar(id, command);

        // 5. Retorna 200 OK com o corpo se atualizou, ou 404 Not Found se não encontrou
        return disciplinaAtualizadaOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
