package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.core.models.Disciplina;

import java.util.UUID;

/**
 * Interface que define o caso de uso para a criação de uma nova {@link Disciplina}.
 * As classes que implementarem esta interface serão responsáveis por orquestrar
 * a lógica necessária para criar uma disciplina no sistema.
 */
public interface CriarDisciplinaUseCase {

    /**
     * Executa a criação de uma nova {@link Disciplina} com base nos dados fornecidos
     * no {@link CriarDisciplinaCommand}.
     *
     * @param command O DTO contendo os dados necessários para criar a disciplina.
     * @return A {@link Disciplina} criada e persistida.
     */
    Disciplina executar(CriarDisciplinaCommand command, UUID usuarioId);
}