package com.pdfocus.application.usuario.port.saida;

import com.pdfocus.core.models.Usuario;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository {

    // Método usado para salvar o token na hora do cadastro
    void salvarToken(Usuario usuario, String token, LocalDateTime expiraEm);

    // Método usado para descobrir quem é o dono do token
    Optional<Usuario> buscarUsuarioPorToken(String token);

    // Método que verifica se o token existe, não expirou e não foi usado
    boolean isTokenValido(String token);

    // Método que marca o token como confirmado no banco
    void confirmarToken(String token);
}