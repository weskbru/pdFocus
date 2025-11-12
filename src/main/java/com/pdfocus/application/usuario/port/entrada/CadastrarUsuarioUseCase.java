package com.pdfocus.application.usuario.port.entrada;

import com.pdfocus.application.usuario.dto.CadastrarUsuarioCommand;
import com.pdfocus.core.models.Usuario;

/**
 * Porta de entrada (Use Case) responsável por registrar um novo usuário no sistema.
 *
 * <p>Implementações desta interface devem:
 * <ul>
 *     <li>Validar os dados fornecidos no comando;</li>
 *     <li>Verificar se o e-mail já está cadastrado;</li>
 *     <li>Persistir o novo usuário no repositório;</li>
 *     <li>Retornar o {@link Usuario} criado.</li>
 * </ul></p>
 */
public interface CadastrarUsuarioUseCase {

    /**
     * Executa o cadastro de um novo usuário.
     *
     * @param command Comando {@link CadastrarUsuarioCommand} contendo os dados do usuário a ser criado.
     * @return O {@link Usuario} recém-criado e persistido.
     * @throws // Futuramente, pode lançar EmailJaCadastradoException se o e-mail já existir.
     */
    Usuario executar(CadastrarUsuarioCommand command);
}
