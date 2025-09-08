package com.pdfocus.application.disciplina.port.entrada;

import java.util.UUID;

/**
 * Define o contrato (Porta de Entrada) para o caso de uso de apagar
 * uma disciplina existente, garantindo que ela pertença ao
 * utilizador autenticado.
 */
public interface DeletarDisciplinaUseCase {

    /**
     * Executa a lógica de negócio para apagar uma disciplina específica.
     * A segurança (garantir que a disciplina pertence ao utilizador logado) é uma
     * responsabilidade da implementação.
     *
     * @param id O UUID da disciplina a ser apagada.
     */
    void executar(UUID id);
}
