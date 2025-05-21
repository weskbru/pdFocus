package com.pdfocus.application.resumo.service;


import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.port.entrada.CriarResumoUseCase;
import com.pdfocus.application.resumo.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;

import java.util.UUID;

public class CriarResumoService implements CriarResumoUseCase {

    private final ResumoRepository resumoRepository;
    private  final DisciplinaRepository disciplinaRepository;

    public CriarResumoService(ResumoRepository resumoRepository, DisciplinaRepository disciplinaRepository){
        this.resumoRepository = resumoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }


    @Override
    public Resumo execute(CriarResumoCommand command) {

        Disciplina disciplina = disciplinaRepository.findById(command.getIdDisciplina())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        UUID id = UUID.randomUUID();

        Resumo resumo = new Resumo(
                id,
                command.getIdUsuario(),
                command.getTitulo(),
                command.getConteudo(),
                disciplina
        );

        resumoRepository.salvar(resumo);
        return resumo;
    }


}
