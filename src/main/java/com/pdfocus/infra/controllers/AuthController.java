package com.pdfocus.infra.controllers;

import com.pdfocus.application.usuario.dto.CadastrarUsuarioCommand;
import com.pdfocus.application.usuario.dto.UsuarioResponse;
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

    public AuthController(CadastrarUsuarioUseCase cadastrarUsuarioUseCase) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
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
}
