package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.core.models.Disciplina;
import java.util.List;
import java.util.UUID;

/**
 * Caso de uso para listar as disciplinas de um usuário específico.
 */
public interface ListarDisciplinasUseCase {

    /**
     * Executa a busca de todas as disciplinas pertencentes a um usuário específico.
     *
     * @param usuarioId O identificador único do usuário. Não pode ser nulo.
     * @return Uma lista de {@link Disciplina} do usuário.
     */
    List<Disciplina> executar(UUID usuarioId);
}