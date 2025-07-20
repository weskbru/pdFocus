package com.pdfocus.core.exceptions;

import java.util.UUID;

/**
 * Exceção lançada quando uma operação tenta acessar um Resumo
 * que não existe no repositório.
 */
public class ResumoNaoEncontradoException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem personalizada.
     * @param mensagem A mensagem de erro.
     */
    public ResumoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor que cria uma mensagem de erro padrão com base no ID do resumo.
     * @param resumoId O ID do resumo que não foi encontrado.
     */
    public ResumoNaoEncontradoException(UUID resumoId) {
        super("Resumo não encontrado com ID: " + resumoId);
    }
}
