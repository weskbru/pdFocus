package com.pdfocus.application.usuario.port.entrada;

import com.pdfocus.application.usuario.dto.CadastrarUsuarioCommand;
import com.pdfocus.core.models.Usuario;

/**
 * Caso de uso para registrar um novo usuário no sistema.
 */
public interface CadastrarUsuarioUseCase {

    /**
     * Executa o processo de cadastro de um novo usuário.
     *
     * @param command O comando com os dados do usuário a ser registrado.
     * @return O objeto {@link Usuario} recém-criado e persistido.
     * @throws // Futuramente, podemos definir exceções como EmailJaCadastradoException.
     */
    Usuario executar(CadastrarUsuarioCommand command);
}
