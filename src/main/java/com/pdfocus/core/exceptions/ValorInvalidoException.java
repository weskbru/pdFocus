package com.pdfocus.core.exceptions;

/**
 * Exceção lançada quando um campo contém um valor fora do domínio esperado.
 * <p>
 * Usada para sinalizar violações de regras de negócio — por exemplo,
 * notas fora do intervalo permitido ou tipos inválidos de feedback.
 * </p>
 *
 * <p>
 * Exemplo de uso:
 * <pre>
 * if (rating < 1 || rating > 5) {
 *     throw new ValorInvalidoException("Rating deve estar entre 1 e 5");
 * }
 * </pre>
 * </p>
 *
 * @see com.pdfocus.core.exceptions.CampoNuloException
 * @see com.pdfocus.core.exceptions.CampoVazioException
 */
public class ValorInvalidoException extends RuntimeException {

    /**
     * Cria uma nova exceção indicando um valor inválido em uma operação de domínio.
     *
     * @param message Mensagem descritiva do erro.
     */
    public ValorInvalidoException(String message) {
        super(message);
    }
}
