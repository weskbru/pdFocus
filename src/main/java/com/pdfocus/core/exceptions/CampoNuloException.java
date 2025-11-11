package com.pdfocus.core.exceptions;

/**
 * Exceção lançada quando um campo obrigatório é {@code null}.
 * <p>
 * Usada pelas entidades de domínio para reforçar invariantes
 * e garantir consistência nos objetos criados.
 * </p>
 *
 * <p>
 * Exemplo de uso:
 * <pre>
 * if (nome == null) throw new CampoNuloException("Nome não pode ser nulo");
 * </pre>
 * </p>
 *
 * @see com.pdfocus.core.exceptions.CampoVazioException
 * @see com.pdfocus.core.exceptions.ValorInvalidoException
 */
public class CampoNuloException extends RuntimeException {

    /**
     * Cria uma nova exceção informando o campo que está nulo.
     *
     * @param message Mensagem descritiva do erro.
     */
    public CampoNuloException(String message) {
        super(message);
    }
}
