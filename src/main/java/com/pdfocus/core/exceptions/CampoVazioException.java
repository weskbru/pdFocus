package com.pdfocus.core.exceptions;

/**
 * Exceção lançada quando um campo obrigatório é informado,
 * mas está vazio ou contém apenas espaços em branco.
 * <p>
 * Essa exceção reforça as regras de consistência do domínio,
 * garantindo que dados essenciais sejam sempre preenchidos de forma válida.
 * </p>
 *
 * <p>
 * Exemplo de uso:
 * <pre>
 * if (nome.isBlank()) throw new CampoVazioException("Nome não pode estar em branco");
 * </pre>
 * </p>
 *
 * @see com.pdfocus.core.exceptions.CampoNuloException
 * @see com.pdfocus.core.exceptions.ValorInvalidoException
 */
public class CampoVazioException extends RuntimeException {

    /**
     * Cria uma nova exceção indicando que um campo obrigatório está vazio.
     *
     * @param message Mensagem descritiva do erro.
     */
    public CampoVazioException(String message) {
        super(message);
    }
}
