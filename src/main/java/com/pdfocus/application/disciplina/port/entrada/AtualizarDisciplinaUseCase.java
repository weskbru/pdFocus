package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.core.models.Disciplina;

import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso para atualizar os dados de uma disciplina existente.
 */
public interface AtualizarDisciplinaUseCase {

    /**
     * Executa a atualização de uma disciplina.
     *
     * @param id O ID da disciplina a ser atualizada.
     * @param command O comando contendo os novos dados para a disciplina.
     * @return Um {@link Optional} contendo a {@link Disciplina} atualizada se a original for encontrada,
     * ou um Optional vazio se nenhuma disciplina com o ID fornecido for encontrada.
     */
    Optional<Disciplina> executar(UUID id, AtualizarDisciplinaCommand command);
}
