package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.core.models.Disciplina;

import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso para obter uma única disciplina pelo seu identificador,
 * garantindo que ela pertença ao usuário especificado.
 */
public interface ObterDisciplinaPorIdUseCase {
    /**
     * Busca uma disciplina com base no seu ID e no ID do usuário proprietário.
     *
     * @param id O identificador único da disciplina a ser buscada.
     * @param usuarioId O identificador único do usuário que deve ser o proprietário da disciplina.
     * @return Um {@link Optional} contendo a {@link Disciplina} se encontrada e se pertencer ao usuário,
     * ou um Optional vazio caso contrário.
     */
    Optional<Disciplina> executar(UUID id, UUID usuarioId);
}
