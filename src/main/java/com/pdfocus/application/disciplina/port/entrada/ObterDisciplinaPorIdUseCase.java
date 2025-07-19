package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.core.models.Disciplina;

import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso para obter uma única disciplina pelo seu identificador.
 */
public interface ObterDisciplinaPorIdUseCase {
    /**
     * Busca uma disciplina com base no ID fornecido.
     *
     * @param id O identificador único da disciplina a ser buscada.
     * @return Um {@link Optional} contendo a {@link Disciplina} se encontrada,
     * ou um Optional vazio caso contrário.
     */
    Optional<Disciplina> executar(UUID id);
}
