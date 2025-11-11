package com.pdfocus.core.exceptions;

/**
 * Exceção lançada quando ocorre uma falha no envio do email de feedback.
 * Segue o padrão de separar exceções de domínio das de infraestrutura.
 */
public class EmailFeedbackException extends RuntimeException {

  public EmailFeedbackException(String message) {
    super(message);
  }

  public EmailFeedbackException(String message, Throwable cause) {
    super(message, cause);
  }

  // Factory methods para casos comuns
  public static EmailFeedbackException falhaEnvio(String destinatario) {
    return new EmailFeedbackException(
            "Falha ao enviar email de feedback para: " + destinatario
    );
  }

  public static EmailFeedbackException configuracaoInvalida() {
    return new EmailFeedbackException(
            "Configuração de email inválida. Verifique as propriedades de configuração"
    );
  }

  public static EmailFeedbackException templateNaoEncontrado(String templateName) {
    return new EmailFeedbackException(
            "Template de email não encontrado: " + templateName
    );
  }
}