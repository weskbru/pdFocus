package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.core.models.Disciplina;

import java.util.UUID;


/**
 * Caso de uso para atualizar os dados de uma disciplina existente.
 */
public interface AtualizarDisciplinaUseCase {

    /**
     * Executa a atualização de uma disciplina PARA O USUÁRIO LOGADO.
     *
     * @param id      O ID da disciplina a ser atualizada.
     * @param command O DTO com os novos dados da disciplina.
     * @return A disciplina atualizada.
     */
    Disciplina executar(UUID id, AtualizarDisciplinaCommand command);
}
