package com.pdfocus.application.exceptions;

public class DisciplinaNaoEncontradaException extends RuntimeException{

    public DisciplinaNaoEncontradaException(String mensagem){
        super(mensagem);
    }

    public DisciplinaNaoEncontradaException(){
        super("Disciplina não encontrada.");
    }

}
