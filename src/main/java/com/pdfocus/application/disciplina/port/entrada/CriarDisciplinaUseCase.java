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
     * Executa a lógica de negócio para criar e persistir uma nova disciplina.
     * A identidade do usuário é obtida implicitamente a partir do contexto de
     * segurança, garantindo que a disciplina seja associada ao proprietário correto.
     *
     * @param command O DTO contendo os dados para a nova disciplina.
     * @return O objeto de domínio {@link Disciplina} recém-criado.
     */
    Disciplina executar(CriarDisciplinaCommand command);
}