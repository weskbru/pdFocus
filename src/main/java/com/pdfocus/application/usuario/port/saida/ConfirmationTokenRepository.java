package com.pdfocus.application.usuario.port.saida;

import com.pdfocus.core.models.Usuario;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository {
    void salvarToken(Usuario usuario, String token, LocalDateTime expiraEm);

    // NOVO: Busca o dono do token para podermos ativar a conta dele
    Optional<Usuario> buscarUsuarioPorToken(String token);

    // NOVO: Verifica se o token já expirou ou já foi usado
    boolean isTokenValido(String token);

    void confirmarToken(String token);
}