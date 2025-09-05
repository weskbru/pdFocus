package com.pdfocus.application.usuario.port.entrada;

import com.pdfocus.core.models.Usuario;

/**
 * Define o contrato para o caso de uso de buscar as informações
 * do usuário atualmente autenticado no sistema.
 */
public interface BuscarUsuarioLogadoUseCase {
    /**
     * Executa a lógica para encontrar e retornar o usuário logado.
     * A identidade do usuário é obtida a partir do contexto de segurança da requisição.
     *
     * @return O objeto de domínio {@link Usuario} correspondente ao usuário autenticado.
     * @throws IllegalStateException se um usuário estiver autenticado mas não for
     * encontrado no banco de dados, indicando uma inconsistência de dados.
     */
    Usuario executar();
}
