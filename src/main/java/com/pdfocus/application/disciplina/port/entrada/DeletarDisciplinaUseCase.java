package com.pdfocus.application.disciplina.port.entrada;

import java.util.UUID;

/**
 * Caso de uso para deletar uma disciplina com base no seu identificador.
 */
public interface DeletarDisciplinaUseCase {

    /**
     * Executa a operação de deleção para a disciplina com o ID fornecido.
     *
     * @param id O identificador único da disciplina a ser deletada.
     * @throws com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException se nenhuma disciplina com o ID fornecido for encontrada.
     * (Definir esta exceção no contrato é uma boa prática).
     */
    void executar(UUID id);
}