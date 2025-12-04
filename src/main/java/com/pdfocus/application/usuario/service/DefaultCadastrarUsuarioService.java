package com.pdfocus.application.usuario.service;

import com.pdfocus.application.usuario.dto.CadastrarUsuarioCommand;
import com.pdfocus.application.usuario.port.entrada.CadastrarUsuarioUseCase;
import com.pdfocus.application.usuario.port.saida.AuthEmailPort;
import com.pdfocus.application.usuario.port.saida.ConfirmationTokenRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.usuario.EmailJaCadastradoException;
import com.pdfocus.core.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso de cadastro.
 *
 * <p>Responsabilidades atualizadas:</p>
 * <ul>
 * <li>Validação e Criptografia (Padrão);</li>
 * <li>Persistência do Usuário;</li>
 * <li><b>Geração e Persistência do Token de Confirmação;</b></li>
 * <li><b>Envio do E-mail de Boas-vindas.</b></li>
 * </ul>
 */
@Service
public class DefaultCadastrarUsuarioService implements CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository tokenRepository;
    private final AuthEmailPort authEmailPort;

    // Injeta a URL correta dependendo do ambiente (dev ou prod)
    @Value("${app.frontend.url}")
    private String frontendUrl;

    public DefaultCadastrarUsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            ConfirmationTokenRepository tokenRepository,
            AuthEmailPort authEmailPort) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.authEmailPort = authEmailPort;
    }

    @Override
    @Transactional
    public Usuario executar(CadastrarUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de cadastro não pode ser nulo.");

        // 1. Valida duplicidade
        usuarioRepository.buscarPorEmail(command.email())
                .ifPresent(usuarioExistente -> {
                    throw new EmailJaCadastradoException(command.email());
                });

        // 2. Cria e Salva o Usuário
        String senhaCriptografada = passwordEncoder.encode(command.senha());
        Usuario novoUsuario = new Usuario(
                command.nome(),
                command.email(),
                senhaCriptografada
        );

        // Define como não verificado (se tiver esse campo no futuro)
        // novoUsuario.setAtivo(false);

        Usuario usuarioSalvo = usuarioRepository.salvar(novoUsuario);

        // 3. Gera e Salva o Token de Confirmação
        String token = UUID.randomUUID().toString();

        // Token expira em 15 minutos
        tokenRepository.salvarToken(usuarioSalvo, token, LocalDateTime.now().plusMinutes(15));

        // 4. Monta o Link e Envia o E-mail
        // O link será algo como: http://localhost:4200/confirmar-email?token=xyz
        String linkConfirmacao = frontendUrl + "/confirmar-email?token=" + token;

        try {
            authEmailPort.enviarEmailConfirmacao(
                    usuarioSalvo.getEmail(),
                    usuarioSalvo.getNome(),
                    linkConfirmacao
            );
        } catch (Exception e) {
            // Loga o erro, mas não impede o cadastro (Fail-safe)
            // O usuário pode pedir reenvio depois
            System.err.println("⚠️ Falha ao enviar e-mail de confirmação: " + e.getMessage());
        }

        return usuarioSalvo;
    }
}