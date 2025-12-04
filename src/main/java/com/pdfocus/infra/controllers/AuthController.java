package com.pdfocus.infra.controllers;

import com.pdfocus.application.usuario.dto.AutenticarUsuarioCommand;
import com.pdfocus.application.usuario.dto.AuthenticationResponse;
import com.pdfocus.application.usuario.dto.CadastrarUsuarioCommand;
import com.pdfocus.application.usuario.dto.UsuarioResponse;
import com.pdfocus.application.usuario.port.entrada.AutenticarUsuarioUseCase;
import com.pdfocus.application.usuario.port.entrada.CadastrarUsuarioUseCase;
import com.pdfocus.application.usuario.port.entrada.ConfirmarEmailUseCase;
import com.pdfocus.core.models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controlador REST responsável pelas operações de autenticação e registro de usuários.
 * <p>
 * Atua como ponto de entrada da API para o módulo de segurança/autenticação.
 * </p>
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final ConfirmarEmailUseCase confirmarEmailService; // Dependência adicionada

    /**
     * Construtor com todas as dependências injetadas.
     * Corrigido: Agora inclui o ConfirmarEmailUseCase.
     */
    public AuthController(
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            AutenticarUsuarioUseCase autenticarUsuarioUseCase,
            ConfirmarEmailUseCase confirmarEmailService // <--- Adicionado aqui
    ) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.confirmarEmailService = confirmarEmailService; // <--- Inicializado aqui
    }

    /**
     * Endpoint para registrar um novo usuário.
     * Retorna 201 Created.
     */
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody CadastrarUsuarioCommand command) {
        Usuario novoUsuario = cadastrarUsuarioUseCase.executar(command);
        UsuarioResponse response = UsuarioResponse.fromDomain(novoUsuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/usuarios/{id}")
                .buildAndExpand(novoUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Endpoint para autenticar (Login).
     * Retorna 200 OK com Token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> autenticar(@RequestBody AutenticarUsuarioCommand command) {
        AuthenticationResponse response = autenticarUsuarioUseCase.executar(command);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para confirmar o e-mail via token.
     * AGORA RETORNA O TOKEN JWT PARA AUTO-LOGIN.
     */
    @PostMapping("/confirm-email")
    public ResponseEntity<AuthenticationResponse> confirmarEmail(@RequestParam("token") String token) {
        // O service agora retorna um objeto com token, nome e email
        AuthenticationResponse response = confirmarEmailService.executar(token);

        // Retornamos esse objeto (JSON) para o Frontend
        return ResponseEntity.ok(response);
    }
}