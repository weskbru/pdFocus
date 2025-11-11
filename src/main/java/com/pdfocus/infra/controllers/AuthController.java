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

/**
 * Controlador REST responsável pelas operações de autenticação e registro de usuários.
 * <p>
 * Atua como ponto de entrada da API para o módulo de segurança/autenticação,
 * delegando toda a lógica de negócio para os casos de uso da camada de aplicação.
 * </p>
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li><b>POST /auth/register</b> — Cria um novo usuário.</li>
 *   <li><b>POST /auth/login</b> — Autentica um usuário e retorna um token JWT.</li>
 * </ul>
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    /**
     * Construtor que injeta as dependências dos casos de uso necessários.
     *
     * @param cadastrarUsuarioUseCase Caso de uso responsável por registrar novos usuários.
     * @param autenticarUsuarioUseCase Caso de uso responsável por autenticar usuários e gerar tokens JWT.
     */
    public AuthController(
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            AutenticarUsuarioUseCase autenticarUsuarioUseCase
    ) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    /**
     * Endpoint para registrar um novo usuário no sistema.
     * <p>
     * Recebe um comando contendo as informações de cadastro,
     * delega o processamento ao caso de uso e retorna uma resposta
     * contendo os dados públicos do novo usuário criado.
     * </p>
     *
     * @param command Objeto contendo os dados de cadastro do usuário.
     * @return 201 (Created) com os dados do usuário criado e o header Location apontando para o recurso recém-criado.
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
     * Endpoint para autenticar um usuário e retornar um token JWT.
     * <p>
     * O caso de uso {@link AutenticarUsuarioUseCase} é responsável por validar
     * as credenciais e gerar o token. Nenhuma lógica de autenticação é feita no controller.
     * </p>
     *
     * @param command Objeto contendo e-mail e senha do usuário.
     * @return 200 (OK) com o token JWT e dados básicos do usuário autenticado.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> autenticar(@RequestBody AutenticarUsuarioCommand command) {
        AuthenticationResponse response = autenticarUsuarioUseCase.executar(command);
        return ResponseEntity.ok(response);
    }
}
