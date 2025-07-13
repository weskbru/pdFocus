package com.pdfocus.infra.controllers;

import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.port.entrada.CriarResumoUseCase;
import com.pdfocus.application.resumo.port.entrada.ListarResumosUseCase;
import com.pdfocus.core.models.Resumo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
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

    public ResumoController(
            ListarResumosUseCase listarResumosUseCase,
            CriarResumoUseCase criarResumoUseCase) {
        this.listarResumosUseCase = listarResumosUseCase;
        this.criarResumoUseCase = criarResumoUseCase;
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

        // 5. Constrói a URI para o novo recurso
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoResumo.getId())
                .toUri();

        // 6. Retorna a resposta 201 Created
        return ResponseEntity.created(location).body(novoResumo);
    }
}
