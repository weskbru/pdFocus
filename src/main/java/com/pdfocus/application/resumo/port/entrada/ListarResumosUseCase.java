package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.core.models.Resumo;

import java.util.List;
import java.util.UUID;

public interface ListarResumosUseCase {

    /**
     * Busca todos os resumos pertencentes a um usuário específico.
     *
     * @param usuarioId O ID do usuário.
     * @return Uma lista de resumos do usuário.
     */
    List<Resumo> buscarTodosPorUsuario(UUID usuarioId);

    /**
     * Busca todos os resumos de uma disciplina específica que pertencem a um usuário.
     *
     * @param disciplinaId O ID da disciplina para filtrar.
     * @param usuarioId O ID do usuário proprietário dos resumos.
     * @return Uma lista de resumos filtrados por disciplina e usuário.
     */
    List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);
}
