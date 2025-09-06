package com.pdfocus.infra.controllers;

import com.pdfocus.application.usuario.dto.UsuarioDetalhesResponse;
import com.pdfocus.application.usuario.port.entrada.BuscarUsuarioLogadoUseCase;
import com.pdfocus.core.models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para gerenciar operações relacionadas a usuários.
 * Este é a interface com o mundo web.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final BuscarUsuarioLogadoUseCase buscarUsuarioLogadoUseCase;

    public UsuarioController(BuscarUsuarioLogadoUseCase buscarUsuarioLogadoUseCase) {
        this.buscarUsuarioLogadoUseCase = buscarUsuarioLogadoUseCase;
    }

    /**
     * Endpoint para buscar os detalhes do usuário atualmente autenticado.
     * Responde a requisições GET em /usuarios/me.
     *
     * @return Resposta 200 (OK) com os detalhes do usuário em formato JSON.
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioDetalhesResponse> buscarUsuarioLogado() {
        Usuario usuarioLogado = buscarUsuarioLogadoUseCase.executar();
        // para algo útil (o DTO de resposta que será convertido em JSON).
        UsuarioDetalhesResponse response = UsuarioDetalhesResponse.fromDomain(usuarioLogado);
        return ResponseEntity.ok(response);
    }
}
