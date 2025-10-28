package com.pdfocus.application.feedback.service;

import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import org.springframework.stereotype.Service;

/**
 * Serviço especializado para envio de emails de feedback.
 * Segue o Single Responsibility Principle - apenas responsável por emails.
 *
 * Vantagens:
 * - Fácil de mockar em testes
 * - Pode ser reutilizado por outros use cases
 * - Evolui independentemente (ex: adicionar templates, configs SMTP)
 */
@Service
public class FeedbackEmailService {

    /**
     * Envia email de notificação para o time sobre novo feedback recebido.
     * Por enquanto apenas log, será implementado posteriormente com JavaMailSender.
     *
     * @param feedback Feedback recebido
     * @throws EmailFeedbackException se houver falha no envio (futuramente)
     */
    public void enviarEmailFeedback(Feedback feedback) {
        try {
            // TODO: Implementar com JavaMailSender quando configurado
            // Por enquanto apenas simulação para não quebrar o fluxo
            simularEnvioEmail(feedback);

        } catch (Exception e) {
            // Em produção, lançaria EmailFeedbackException
            // Por enquanto apenas log para não quebrar o fluxo principal
            System.err.println("❌ Erro ao enviar email de feedback: " + e.getMessage());
        }
    }

    /**
     * Simula o envio de email (apenas para desenvolvimento).
     * Será substituído por JavaMailSender real no futuro.
     */
    private void simularEnvioEmail(Feedback feedback) {
        System.out.println("📧 EMAIL DE FEEDBACK (SIMULAÇÃO)");
        System.out.println("Para: feedback@pdfocus.com");
        System.out.println("Assunto: Novo Feedback - " + feedback.getTipo());
        System.out.println("De: " + (feedback.getEmailUsuario() != null ? feedback.getEmailUsuario() : "Anônimo"));
        System.out.println("Mensagem: " + feedback.getMensagem());
        System.out.println("Rating: " + (feedback.getRating() != null ? feedback.getRating() + "⭐" : "N/A"));
        System.out.println("Página: " + feedback.getPagina());
        System.out.println("Data: " + feedback.getDataCriacao());
        System.out.println("─────────────────────────────");
    }
}