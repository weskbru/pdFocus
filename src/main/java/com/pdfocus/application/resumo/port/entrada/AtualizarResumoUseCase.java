package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.application.resumo.dto.AtualizarResumoCommand;
import com.pdfocus.core.models.Resumo;

import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso para atualizar os dados de um resumo existente.
 */
public interface AtualizarResumoUseCase {

    /**
     * Executa a atualização de um resumo.
     *
     * @param id O ID do resumo a ser atualizado.
     * @param usuarioId O ID do usuário que deve ser o proprietário do resumo.
     * @param command O comando contendo os novos dados para o resumo.
     * @return Um {@link Optional} contendo o {@link Resumo} atualizado se ele for encontrado
     * e pertencer ao usuário, ou um Optional vazio caso contrário.
     */
    Optional<Resumo> executar(UUID id, UUID usuarioId, AtualizarResumoCommand command);

}
