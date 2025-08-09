package com.pdfocus.core.exceptions;

public class TituloObrigatorioException  extends RuntimeException{

    public TituloObrigatorioException(String mensagem){
        super(mensagem);
    }

    public TituloObrigatorioException() {
        super("Título é obrigatório");
    }
}
