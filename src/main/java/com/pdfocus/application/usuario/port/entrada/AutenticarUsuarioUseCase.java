package com.pdfocus.application.usuario.port.entrada;

import com.pdfocus.application.usuario.dto.AutenticarUsuarioCommand;
import com.pdfocus.application.usuario.dto.AuthenticationResponse;

/**
 * Porta de entrada (Use Case) responsável por autenticar um usuário no sistema.
 *
 * <p>Implementações desta interface devem:
 * <ul>
 *     <li>Validar as credenciais fornecidas;</li>
 *     <li>Gerar um token JWT para sessões autenticadas;</li>
 *     <li>Lançar exceções apropriadas caso a autenticação falhe.</li>
 * </ul></p>
 */
public interface AutenticarUsuarioUseCase {

    /**
     * Executa o processo de autenticação do usuário com base nas credenciais fornecidas.
     *
     * @param command Comando {@link AutenticarUsuarioCommand} contendo e-mail e senha do usuário.
     * @return {@link AuthenticationResponse} contendo o token JWT gerado se a autenticação for bem-sucedida.
     * @throws // Futuramente, pode lançar CredenciaisInvalidasException ou outras exceções de autenticação.
     */
    AuthenticationResponse executar(AutenticarUsuarioCommand command);
}
