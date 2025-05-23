package com.pdfocus.core.exceptions;

public class ConteudoObrigatorioException extends RuntimeException{
    public ConteudoObrigatorioException(String mensagem){
        super(mensagem);
    }

    public ConteudoObrigatorioException(){
        super("Disciplina não encontrada.");
    }
}
