package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.core.models.Resumo;

import java.util.List;
import java.util.UUID;

/**
 * Porta de entrada (Use Case) responsável pela leitura de {@link Resumo}s.
 * <p>
 * Fornece métodos para listar resumos de um usuário,
 * podendo filtrar por disciplina quando necessário.
 * </p>
 */
public interface ListarResumosUseCase {

    /**
     * Lista todos os resumos pertencentes a um usuário autenticado.
     *
     * @param usuarioId O identificador único do usuário.
     * @return Uma lista de {@link Resumo} pertencentes ao usuário.
     */
    List<Resumo> buscarTodosPorUsuario(UUID usuarioId);

    /**
     * Lista todos os resumos de uma disciplina específica
     * que pertencem ao usuário autenticado.
     *
     * @param disciplinaId O identificador único da disciplina.
     * @param usuarioId    O identificador do usuário proprietário dos resumos.
     * @return Uma lista de {@link Resumo} filtrada por disciplina e usuário.
     */
    List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);
}
