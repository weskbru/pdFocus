package com.pdfocus.infra.controllers;

import com.pdfocus.application.usuario.dto.UsuarioDetalhesResponse;
import com.pdfocus.application.usuario.port.entrada.BuscarUsuarioLogadoUseCase;
import com.pdfocus.core.models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável pelas operações relacionadas ao usuário autenticado.
 * <p>
 * Expõe endpoints para que o cliente (frontend) possa obter as informações do usuário
 * logado. Toda a lógica de autenticação e resolução do usuário é tratada na camada
 * de aplicação através do {@link BuscarUsuarioLogadoUseCase}.
 * </p>
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final BuscarUsuarioLogadoUseCase buscarUsuarioLogadoUseCase;

    public UsuarioController(BuscarUsuarioLogadoUseCase buscarUsuarioLogadoUseCase) {
        this.buscarUsuarioLogadoUseCase = buscarUsuarioLogadoUseCase;
    }

    /**
     * Retorna os detalhes do usuário atualmente autenticado.
     * <p>
     * Este endpoint é essencial para o carregamento do perfil do usuário no frontend
     * (ex: nome, e-mail e data de cadastro). A autenticação é feita via token JWT,
     * e a camada de aplicação resolve o usuário a partir do contexto de segurança.
     * </p>
     *
     * @return 200 (OK) com os detalhes do usuário em {@link UsuarioDetalhesResponse}.
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioDetalhesResponse> buscarUsuarioLogado() {
        Usuario usuarioLogado = buscarUsuarioLogadoUseCase.executar();

        UsuarioDetalhesResponse response = UsuarioDetalhesResponse.fromDomain(usuarioLogado);

        return ResponseEntity.ok(response);
    }
}
