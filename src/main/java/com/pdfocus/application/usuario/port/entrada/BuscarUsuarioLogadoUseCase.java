package com.pdfocus.application.usuario.port.entrada;

import com.pdfocus.core.models.Usuario;

/**
 * Porta de entrada (Use Case) responsável por buscar as informações
 * do usuário atualmente autenticado no sistema.
 *
 * <p>Implementações desta interface devem:
 * <ul>
 *     <li>Obter a identidade do usuário a partir do contexto de segurança da requisição;</li>
 *     <li>Recuperar os dados completos do usuário no repositório;</li>
 *     <li>Lançar exceções apropriadas em caso de inconsistência.</li>
 * </ul></p>
 */
public interface BuscarUsuarioLogadoUseCase {

    /**
     * Executa a lógica para encontrar e retornar o usuário logado.
     *
     * @return O objeto de domínio {@link Usuario} correspondente ao usuário autenticado.
     * @throws IllegalStateException Se um usuário estiver autenticado, mas não for encontrado
     * no banco de dados, indicando uma inconsistência de dados.
     */
    Usuario executar();
}
