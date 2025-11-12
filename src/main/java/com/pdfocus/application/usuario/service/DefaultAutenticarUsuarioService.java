package com.pdfocus.application.usuario.service;

import com.pdfocus.application.usuario.dto.AutenticarUsuarioCommand;
import com.pdfocus.application.usuario.dto.AuthenticationResponse;
import com.pdfocus.application.usuario.port.entrada.AutenticarUsuarioUseCase;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.config.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementação padrão do caso de uso {@link AutenticarUsuarioUseCase} para autenticação de usuários.
 *
 * <p>Este serviço é responsável por:
 * <ul>
 *     <li>Validar as credenciais do usuário utilizando {@link AuthenticationManager} do Spring Security;</li>
 *     <li>Recuperar os dados completos do usuário no repositório;</li>
 *     <li>Gerar um token JWT válido para sessões autenticadas.</li>
 * </ul></p>
 *
 * <p>Ele garante que apenas usuários com credenciais corretas consigam gerar tokens de autenticação.</p>
 */
@Service
public class DefaultAutenticarUsuarioService implements AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Construtor do serviço.
     *
     * @param usuarioRepository      Repositório de usuários.
     * @param jwtService             Serviço responsável por gerar tokens JWT.
     * @param authenticationManager  Gerenciador de autenticação do Spring Security.
     */
    public DefaultAutenticarUsuarioService(
            UsuarioRepository usuarioRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Executa o caso de uso de autenticação do usuário.
     *
     * <p>O método realiza os seguintes passos:
     * <ol>
     *     <li>Valida o e-mail e a senha fornecidos através do {@link AuthenticationManager};</li>
     *     <li>Recupera o usuário no repositório caso a autenticação seja bem-sucedida;</li>
     *     <li>Gera um token JWT para o usuário autenticado;</li>
     *     <li>Retorna um {@link AuthenticationResponse} contendo o token.</li>
     * </ol></p>
     *
     * @param command Comando {@link AutenticarUsuarioCommand} contendo e-mail e senha do usuário.
     * @return {@link AuthenticationResponse} contendo o token JWT gerado.
     * @throws org.springframework.security.core.AuthenticationException Se as credenciais forem inválidas.
     * @throws IllegalStateException Se o usuário não for encontrado após autenticação bem-sucedida.
     */
    @Override
    public AuthenticationResponse executar(AutenticarUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de autenticação não pode ser nulo.");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.email(),
                        command.senha()
                )
        );

        Usuario usuario = usuarioRepository.buscarPorEmail(command.email())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação bem-sucedida."));

        String jwtToken = jwtService.generateToken(usuario.getEmail());

        return new AuthenticationResponse(jwtToken);
    }
}
