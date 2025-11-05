package com.pdfocus.core.exceptions.disciplina;

import java.util.UUID;

public class DisciplinaNaoEncontradaException extends RuntimeException{

    public DisciplinaNaoEncontradaException(String mensagem){
        super(mensagem);
    }

    // Construtor que recebe o ID da disciplina
    public DisciplinaNaoEncontradaException(UUID disciplinaId) {
        super("Disciplina não encontrada com ID: " + disciplinaId);
    }



}
