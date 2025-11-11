package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.application.resumo.dto.AtualizarResumoCommand;
import com.pdfocus.core.models.Resumo;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta de entrada (Use Case) responsável por atualizar os dados de um resumo existente.
 *
 * <p>Implementações desta interface devem garantir que apenas o usuário proprietário
 * possa atualizar o resumo, respeitando a integridade dos dados.</p>
 */
public interface AtualizarResumoUseCase {

    /**
     * Executa a atualização de um resumo.
     *
     * <p>O método recebe o ID do resumo, o ID do usuário proprietário e um comando com os
     * novos dados. Retorna o resumo atualizado se ele existir e pertencer ao usuário,
     * ou um {@link Optional} vazio caso contrário.</p>
     *
     * @param id        O ID do resumo a ser atualizado.
     * @param usuarioId O ID do usuário que deve ser o proprietário do resumo.
     * @param command   O comando contendo os novos dados do resumo.
     * @return Um {@link Optional} contendo o {@link Resumo} atualizado, ou vazio se não encontrado.
     */
    Optional<Resumo> executar(UUID id, UUID usuarioId, AtualizarResumoCommand command);

}
