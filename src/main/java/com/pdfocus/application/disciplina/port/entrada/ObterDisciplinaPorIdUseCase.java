package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;

import java.util.Optional;
import java.util.UUID;

/**
 * Define o contrato (Porta de Entrada) para o caso de uso de obter uma
 * disciplina específica pelo seu ID, garantindo que ela pertença ao
 * usuário autenticado.
 */
public interface ObterDisciplinaPorIdUseCase {
    /**
     * Executa a busca de uma disciplina específica. A segurança é garantida
     * pela implementação, que filtra pelo usuário logado.
     *
     * @param id O UUID da disciplina a ser buscada.
     * @return um Optional contendo a {@link Disciplina} se encontrada e pertencente
     * ao usuário, ou um Optional vazio caso contrário.
     */
    Optional<DetalheDisciplinaResponse> executar(UUID id);
}
