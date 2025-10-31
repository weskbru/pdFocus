package com.pdfocus.application.feedback.port.saida;

import com.pdfocus.core.models.Feedback;

/**
 * Porta de saída para envio de e-mails de feedback.
 * Segue o padrão das outras portas de saída (ex: integração com serviços externos).
 *
 * Responsabilidade: definir o contrato para envio de notificações de feedback.
 */
public interface FeedbackEmailPort {
    void enviarEmailFeedback(Feedback feedback);
}
