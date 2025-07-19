package com.pdfocus.application.resumo.port.entrada;

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
     * @return {@code true} se o resumo foi encontrado e deletado com sucesso,
     * {@code false} caso contrário (não encontrado ou não pertence ao usuário).
     */
    boolean executar(UUID id, UUID usuarioId);
}
