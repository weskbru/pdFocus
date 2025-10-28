package com.pdfocus.core.exceptions;

/**
 * Exceção lançada quando um feedback não atende aos critérios de validação.
 */
public class FeedbackInvalidoException extends RuntimeException {

    public FeedbackInvalidoException(String message) {
        super(message);
    }

    public FeedbackInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    // Factory methods para casos comuns
    public static FeedbackInvalidoException paraTipoInvalido(String tipoRecebido) {
        return new FeedbackInvalidoException(
                "Tipo de feedback inválido: '" + tipoRecebido + "'. " +
                        "Tipos válidos: BUG, SUGGESTION, FEATURE, OTHER"
        );
    }

    public static FeedbackInvalidoException paraRatingInvalido(Integer rating) {
        return new FeedbackInvalidoException(
                "Rating inválido: " + rating + ". " +
                        "O rating deve estar entre 1 e 5"
        );
    }

    public static FeedbackInvalidoException paraMensagemMuitoLonga(int maxLength) {
        return new FeedbackInvalidoException(
                "Mensagem do feedback muito longa. " +
                        "Máximo permitido: " + maxLength + " caracteres"
        );
    }
}