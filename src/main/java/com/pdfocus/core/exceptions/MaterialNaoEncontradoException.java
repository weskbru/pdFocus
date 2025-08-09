package com.pdfocus.core.exceptions;

import java.util.UUID;

/**
 * Exceção lançada quando uma operação tenta aceder a um Material
 * que não existe no repositório.
 */
public class MaterialNaoEncontradoException extends RuntimeException {
    /**
     * Construtor que aceita uma mensagem personalizada.
     * @param mensagem A mensagem de erro.
     */
    public MaterialNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor que cria uma mensagem de erro padrão com base no ID do material.
     * @param materialId O ID do material que não foi encontrado.
     */
    public MaterialNaoEncontradoException(UUID materialId) {
        super("Material não encontrado com o ID: " + materialId);
    }
}
