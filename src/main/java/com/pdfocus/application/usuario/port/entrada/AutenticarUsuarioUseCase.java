package com.pdfocus.application.usuario.port.entrada;

import com.pdfocus.application.usuario.dto.AutenticarUsuarioCommand;
import com.pdfocus.application.usuario.dto.AuthenticationResponse;

/**
 * Caso de uso para autenticar um usuário no sistema.
 */
public interface AutenticarUsuarioUseCase {

    /**
     * Executa o processo de autenticação com base nas credenciais fornecidas.
     *
     * @param command O comando contendo o e-mail и a senha do usuário.
     * @return um {@link AuthenticationResponse} contendo o token JWT se a autenticação for bem-sucedida.
     * @throws // Futuramente, podemos definir exceções como CredenciaisInvalidasException.
     */
    AuthenticationResponse executar(AutenticarUsuarioCommand command);
}
