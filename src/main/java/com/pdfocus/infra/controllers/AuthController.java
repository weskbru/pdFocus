package com.pdfocus.infra.controllers;

import com.pdfocus.application.usuario.dto.AutenticarUsuarioCommand;
import com.pdfocus.application.usuario.dto.AuthenticationResponse;
import com.pdfocus.application.usuario.dto.CadastrarUsuarioCommand;
import com.pdfocus.application.usuario.dto.UsuarioResponse;
import com.pdfocus.application.usuario.port.entrada.AutenticarUsuarioUseCase;
import com.pdfocus.application.usuario.port.entrada.CadastrarUsuarioUseCase;
import com.pdfocus.core.models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;


    public AuthController(
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody CadastrarUsuarioCommand command) {
        // Chama o caso de uso para criar o usuário
        Usuario novoUsuario = cadastrarUsuarioUseCase.executar(command);

        // Converte o objeto de domínio para o DTO de resposta segura
        UsuarioResponse response = UsuarioResponse.fromDomain(novoUsuario);

        // Constrói a URI para o novo recurso
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/usuarios/{id}") // Supondo um futuro endpoint /usuarios/{id}
                .buildAndExpand(novoUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Endpoint para autenticar um usuário e retornar um token JWT.
     * Responde a requisições POST em /auth/login.
     *
     * @param command O comando com as credenciais (e-mail e senha).
     * @return Resposta 200 (OK) com o token JWT se a autenticação for bem-sucedida.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> autenticar(@RequestBody AutenticarUsuarioCommand command) {
        // Chama o caso de uso, que retorna a resposta com o token
        AuthenticationResponse response = autenticarUsuarioUseCase.executar(command);
        return ResponseEntity.ok(response);
    }
}
