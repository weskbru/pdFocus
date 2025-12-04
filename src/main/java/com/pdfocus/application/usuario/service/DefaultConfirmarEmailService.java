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
    private final UsuarioRepository usuarioRepository; // Necessário para salvar o status 'ativo'
    private final JwtService jwtService; // Necessário para gerar o token de auto-login

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
        // 1. Valida se o token existe, não expirou e não foi usado
        if (!tokenRepository.isTokenValido(token)) {
            throw new IllegalArgumentException("Token inválido, expirado ou já utilizado.");
        }

        // 2. Busca o dono do token para ativá-lo
        Usuario usuario = tokenRepository.buscarUsuarioPorToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para este token."));

        // 3. Ativa o usuário (Agora ele pode logar)
        usuario.setAtivo(true);
        usuarioRepository.salvar(usuario);

        // 4. Marca o token como usado (Queima o cartucho)
        tokenRepository.confirmarToken(token);

        // 5. Gera o Token JWT para o Auto-Login (A Mágica!)
        // Nota: Ajuste se o seu JwtService pedir o objeto Usuario ou apenas o email
        String jwtToken = jwtService.generateToken(usuario.getEmail());

        // 6. Retorna os dados para o Frontend entrar no Dashboard
        return new AuthenticationResponse(jwtToken, usuario.getNome(), usuario.getEmail());
    }
}