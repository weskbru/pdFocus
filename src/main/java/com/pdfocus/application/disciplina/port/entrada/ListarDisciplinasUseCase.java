package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.core.models.Disciplina;

import java.util.List;

/**
 * Caso de uso para listar todas as disciplinas existentes.
 */
public  interface ListarDisciplinasUseCase {

    /**
     * Retorna uma lista de todas as disciplinas cadastradas.
     *
     * @return Uma lista contendo todas as disciplinas.
     * Retorna uma lista vazia se nenhuma disciplina estiver cadastrada.
     */
    List<Disciplina> listarTodas();
}
