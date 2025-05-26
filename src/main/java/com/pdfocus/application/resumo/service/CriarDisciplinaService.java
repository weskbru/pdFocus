package com.pdfocus.application.resumo.service;


import com.pdfocus.application.resumo.dto.CriarDisciplinaCommand;
import com.pdfocus.application.resumo.port.entrada.CriarDisciplinaUseCase;
import com.pdfocus.application.resumo.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;

import java.util.UUID;

/**
 * Implementação da interface {@link CriarDisciplinaUseCase}.
 * Responsável por criar uma nova {@link Disciplina} e persistí-la
 * utilizando o {@link DisciplinaRepository}.
 */
public class CriarDisciplinaService implements CriarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    /**
     * Construtor que recebe uma instância de {@link DisciplinaRepository}
     * para realizar a persistência das disciplinas.
     *
     * @param disciplinaRepository O repositório de disciplinas.
     */
    public CriarDisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * Cria uma nova {@link Disciplina} a partir do {@link CriarDisciplinaCommand}
     * e a salva utilizando o {@link DisciplinaRepository}.
     *
     * @param command O comando contendo os dados para a criação da disciplina.
     * @return A {@link Disciplina} criada e salva.
     */
    @Override
    public Disciplina criar(CriarDisciplinaCommand command) {
        UUID novoId = UUID.randomUUID();
        Disciplina novaDisciplina = new Disciplina(novoId, command.getNome(), command.getDescricao());
        return disciplinaRepository.salvar(novaDisciplina);
    }
}