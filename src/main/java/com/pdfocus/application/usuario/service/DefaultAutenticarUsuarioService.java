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
 * Implementação padrão do caso de uso para autenticar um usuário.
 */
@Service
public class DefaultAutenticarUsuarioService implements AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public DefaultAutenticarUsuarioService(
            UsuarioRepository usuarioRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Utiliza o AuthenticationManager do Spring Security para validar as credenciais.
     * Se a autenticação for bem-sucedida, busca o usuário e gera um token JWT.
     * </p>
     * @throws org.springframework.security.core.AuthenticationException se as credenciais forem inválidas.
     */
    @Override
    public AuthenticationResponse executar(AutenticarUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de autenticação não pode ser nulo.");

        // 1 Usa o AuthenticationManager para validar o e-mail e a senha.
        // Se as credenciais estiverem erradas, ele lançará uma AuthenticationException automaticamente.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.email(),
                        command.senha()
                )
        );

        // 2 Se a autenticação passou, significa que as credenciais são válidas.
        // buscamos o usuário no nosso repositório para obter seus dados.
        // Usamos orElseThrow para o caso (muito improvável) de o usuário não ser encontrado após a autenticação.
        Usuario usuario = usuarioRepository.buscarPorEmail(command.email())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação bem-sucedida."));

        // 3. Gera o token JWT para o usuário encontrado.
        String jwtToken = jwtService.generateToken(usuario.getEmail());

        // 4. Retorna a resposta com o token.
        return new AuthenticationResponse(jwtToken);
    }
}