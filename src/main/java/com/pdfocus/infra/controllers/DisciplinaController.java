package com.pdfocus.infra.controllers;
import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;
import com.pdfocus.application.disciplina.dto.DisciplinaResponse;
import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.*;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.infra.security.AuthenticationHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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


    public DisciplinaController(
            ListarDisciplinasUseCase listarDisciplinasUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            ObterDisciplinaPorIdUseCase obterDisciplinaPorIdUseCase,
            DeletarDisciplinaUseCase deletarDisciplinaUseCase,
            AtualizarDisciplinaUseCase atualizarDisciplinaUseCase) {
        this.listarDisciplinasUseCase = listarDisciplinasUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.obterDisciplinaPorIdUseCase = obterDisciplinaPorIdUseCase;
        this.deletarDisciplinaUseCase = deletarDisciplinaUseCase;
        this.atualizarDisciplinaUseCase = atualizarDisciplinaUseCase;

    }

    /**
     * Endpoint para listar todas as disciplinas pertencentes ao usuário autenticado.
     * A identificação do usuário é tratada na camada de aplicação para garantir
     * o isolamento dos dados.
     *
     * @return ResponseEntity com status 200 (OK) e uma lista de {@link DisciplinaResponse}.
     */
    @GetMapping
    public ResponseEntity<List<DisciplinaResponse>> listarDisciplinas() {
        // Delega a execução da lógica de negócio para o caso de uso correspondente.
        List<Disciplina> disciplinas = listarDisciplinasUseCase.executar();

        // Converte a lista de domínio para uma lista de DTOs para a resposta.
        List<DisciplinaResponse> response = disciplinas.stream()
                .map(DisciplinaResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para criar uma nova disciplina para o usuário autenticado.
     * A lógica de identificação e associação do usuário é tratada na camada de serviço.
     *
     * @param command O DTO com os dados para criar a disciplina.
     * @return ResponseEntity com status 201 (Created), a URI do novo recurso no
     * cabeçalho 'Location', e o recurso criado como {@link DisciplinaResponse}.
     */
    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@RequestBody CriarDisciplinaCommand command) {
        // Delega a execução para o caso de uso, que agora lida com a lógica de segurança.
        Disciplina novaDisciplina = criarDisciplinaUseCase.executar(command);

        // Constrói a URI para o novo recurso, seguindo as boas práticas REST.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaDisciplina.getId())
                .toUri();

        // Converte o modelo de domínio para o DTO de resposta antes de retornar.
        DisciplinaResponse response = DisciplinaResponse.fromDomain(novaDisciplina);

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Endpoint para buscar uma única disciplina do usuário autenticado pelo seu ID.
     *
     * ESTE MÉTODO FOI REFATORADO PARA SEGURANÇA.
     * A lógica de segurança que verifica a posse da disciplina foi movida para
     * a camada de serviço.
     *
     * @param id O UUID da disciplina a ser buscada.
     * @return Resposta 200 (OK) com a {@link DisciplinaResponse} se encontrada, ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetalheDisciplinaResponse> obterPorId(
            @PathVariable UUID id,
            Pageable pageable // ✅ 2. ADICIONE ESTE PARÂMETRO
    ) {
        // O código abaixo vai apresentar um erro, pois o 'executar' ainda não
        // espera o 'pageable'. Isso é o que nos guiará para o próximo arquivo.
        return obterDisciplinaPorIdUseCase.executar(id, pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para atualizar uma disciplina existente.
     * A lógica de negócio garante que apenas o proprietário da disciplina pode
     * realizar esta operação.
     *
     * @param id O UUID da disciplina a ser atualizada, vindo da URL.
     * @param command O corpo da requisição (JSON) com os novos dados da disciplina.
     * @return ResponseEntity com status 200 (OK) e a {@link DisciplinaResponse} atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> atualizar(
            @PathVariable UUID id,
            @RequestBody AtualizarDisciplinaCommand command) {

        // Delega a execução para o caso de uso, que contém a lógica de segurança e atualização.
        Disciplina disciplinaAtualizada = atualizarDisciplinaUseCase.executar(id, command);

        // Converte o resultado para o DTO de resposta antes de enviá-lo.
        DisciplinaResponse response = DisciplinaResponse.fromDomain(disciplinaAtualizada);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para deletar uma disciplina existente do usuário autenticado.
     *
     * ESTE MÉTODO FOI REFATORADO PARA SEGURANÇA.
     * A lógica de segurança que verifica a posse da disciplina foi movida para
     * a camada de serviço.
     *
     * @param id O UUID da disciplina a ser deletada.
     * @return Resposta 204 (No Content) se a deleção for bem-sucedida.
     * A camada de serviço lançará uma exceção se a disciplina não for
     * encontrada ou não pertencer ao usuário, resultando em um erro 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        // Delega a execução para o caso de uso, que agora contém a lógica de segurança.
        deletarDisciplinaUseCase.executar(id);

        // Retorna o status 204 No Content, que é a melhor prática para
        // operações de deleção bem-sucedidas.
        return ResponseEntity.noContent().build();
    }
}
