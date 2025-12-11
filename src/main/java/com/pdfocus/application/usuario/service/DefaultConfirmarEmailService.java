package com.pdfocus.application.usuario.service;

import com.pdfocus.application.usuario.dto.AuthenticationResponse;
import com.pdfocus.application.usuario.port.entrada.ConfirmarEmailUseCase;
import com.pdfocus.application.usuario.port.saida.ConfirmationTokenRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.config.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultConfirmarEmailService implements ConfirmarEmailUseCase {

    private final ConfirmationTokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public DefaultConfirmarEmailService(
            ConfirmationTokenRepository tokenRepository,
            UsuarioRepository usuarioRepository,
            JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public AuthenticationResponse executar(String token) {
        // 1. Usa o método que definimos na Interface (Passo 1)
        if (!tokenRepository.isTokenValido(token)) {
            throw new IllegalArgumentException("Token inválido, expirado ou já utilizado.");
        }

        // 2. Busca o dono do token
        Usuario usuario = tokenRepository.buscarUsuarioPorToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para este token."));

        // 3. Ativa o usuário
        usuario.setAtivo(true);
        usuarioRepository.salvar(usuario);

        // 4. Marca o token como usado
        tokenRepository.confirmarToken(token);

        // 5. Gera o Token JWT para login automático
        String jwtToken = jwtService.generateToken(usuario.getEmail());

        return new AuthenticationResponse(jwtToken, usuario.getNome(), usuario.getEmail());
    }
}