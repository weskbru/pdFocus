package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.core.exceptions.resumo.ResumoNaoEncontradoException;

import java.util.UUID;

/**
 * Caso de uso para deletar um resumo existente.
 */
public interface DeletarResumoUseCase {

    /**
     * Executa a deleção de um resumo, garantindo que ele pertença ao usuário.
     *
     * @param id O ID do resumo a ser deletado.
     * @param usuarioId O ID do usuário que deve ser o proprietário do resumo.
     * @throws ResumoNaoEncontradoException se o resumo não for encontrado ou não pertencer ao usuário.
     */
    void executar(UUID id, UUID usuarioId);
}
