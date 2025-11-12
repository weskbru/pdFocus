package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.core.exceptions.resumo.ResumoNaoEncontradoException;

import java.util.UUID;

/**
 * Porta de entrada (Use Case) responsável pela exclusão de um {@code Resumo}.
 * <p>
 * Este caso de uso garante que a operação de deleção só será executada
 * se o resumo pertencer ao usuário autenticado.
 * </p>
 */
public interface DeletarResumoUseCase {

    /**
     * Executa a exclusão de um resumo específico, verificando sua associação
     * com o usuário solicitante.
     *
     * @param id         O identificador único do resumo a ser deletado.
     * @param usuarioId  O identificador único do usuário proprietário.
     * @throws ResumoNaoEncontradoException
     *         Se o resumo não for encontrado ou não pertencer ao usuário informado.
     */
    void executar(UUID id, UUID usuarioId);
}
