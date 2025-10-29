package com.pdfocus.infra.email;

import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Serviço de feedback em MODO DESENVOLVIMENTO
 * (Não envia emails reais devido a bloqueio de rede)
 */
@Service
public class EmailFeedbackService {

    private final String emailDestino;

    public EmailFeedbackService(@Value("${app.feedback.email}") String emailDestino) {
        this.emailDestino = emailDestino;
    }

    public void enviarEmailFeedback(Feedback feedback) {
        try {
            // SIMULA ENVIO DE EMAIL (apenas logs bonitos)
            System.out.println(" ");
            System.out.println("🎯 ===================================================");
            System.out.println("🎯           FEEDBACK RECEBIDO - PDFocus");
            System.out.println("🎯 ===================================================");
            System.out.println("📧 TIPO: " + (feedback.getTipo() != null ? feedback.getTipo() : "Não especificado"));
            System.out.println("👤 USUÁRIO: " + (feedback.getEmailUsuario() != null ? feedback.getEmailUsuario() : "Anônimo"));

            if (feedback.getRating() != null && feedback.getRating() > 0) {
                System.out.println("⭐ AVALIAÇÃO: " + "⭐".repeat(feedback.getRating()) + " (" + feedback.getRating() + "/5)");
            } else {
                System.out.println("⭐ AVALIAÇÃO: N/A");
            }

            System.out.println("💬 MENSAGEM: " + (feedback.getMensagem() != null ? feedback.getMensagem() : "Sem mensagem"));
            System.out.println("📄 PÁGINA: " + (feedback.getPagina() != null ? feedback.getPagina() : "Não especificada"));
            System.out.println("📅 DATA: " + (feedback.getDataCriacao() != null ? feedback.getDataCriacao() : "Data não disponível"));
            System.out.println("🎯 DESTINO: " + emailDestino);
            System.out.println("✅ STATUS: Feedback registrado com sucesso!");
            System.out.println("💡 MODO: Desenvolvimento (emails desativados)");
            System.out.println("🎯 ===================================================");
            System.out.println(" ");

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar feedback: " + e.getMessage());
            throw new EmailFeedbackException("Erro ao processar feedback", e);
        }
    }
}