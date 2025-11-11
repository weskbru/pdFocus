package com.pdfocus.infra.email;

import com.pdfocus.application.feedback.port.saida.FeedbackEmailPort;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResendEmailFeedbackService implements FeedbackEmailPort {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String emailDestino;

    public ResendEmailFeedbackService(
            RestTemplate restTemplate,
            @Value("${app.resend.api-key}") String apiKey,
            @Value("${app.feedback.email}") String emailDestino) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.emailDestino = emailDestino;
    }

    @Override
    public void enviarEmailFeedback(Feedback feedback) {
        // Mostra no console
        mostrarNoConsole(feedback);

        // Monta o JSON do Resend usando e-mail de teste do Resend
        Map<String, Object> body = new HashMap<>();
        body.put("from", "PDFocus Test <onboarding@resend.dev>"); // remetente de teste
        // Depois (domínio próprio)
        // body.put("from", "PDFocus <no-reply@seudominio.com>");
        body.put("to", new String[]{emailDestino});               // seu e-mail de teste
        body.put("subject", "📨 Novo Feedback recebido - PDFocus (Teste)");
        body.put("text", montarCorpoEmail(feedback));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.resend.com/emails",
                    request,
                    String.class
            );

            System.out.println("✅ Feedback enviado para Resend (teste) com status: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar feedback via Resend: " + e.getMessage());
            throw new EmailFeedbackException("Erro ao enviar feedback via Resend", e);
        }
    }

    private void mostrarNoConsole(Feedback feedback) {
        System.out.println("\n🎯 ================= FEEDBACK RECEBIDO =================");
        System.out.println("📧 TIPO: " + (feedback.getTipo() != null ? feedback.getTipo() : "Não especificado"));
        System.out.println("👤 USUÁRIO: " + (feedback.getEmailUsuario() != null ? feedback.getEmailUsuario() : "Anônimo"));
        System.out.println("⭐ AVALIAÇÃO: " + (feedback.getRating() != null ? feedback.getRating() + "/5" : "N/A"));
        System.out.println("💬 MENSAGEM: " + (feedback.getMensagem() != null ? feedback.getMensagem() : "Sem mensagem"));
        System.out.println("📄 PÁGINA: " + (feedback.getPagina() != null ? feedback.getPagina() : "Não informada"));
        System.out.println("📅 DATA: " + (feedback.getDataCriacao() != null ? feedback.getDataCriacao() : "Data não disponível"));
        System.out.println("🎯 DESTINO (teste): " + emailDestino);
        System.out.println("✅ STATUS: Feedback registrado com sucesso!");
        System.out.println("========================================================\n");
    }

    private String montarCorpoEmail(Feedback feedback) {
        StringBuilder sb = new StringBuilder();
        sb.append("📬 NOVO FEEDBACK RECEBIDO\n\n");
        sb.append("📅 Data: ").append(feedback.getDataCriacao()).append("\n");
        sb.append("👤 Usuário: ").append(feedback.getEmailUsuario() != null ? feedback.getEmailUsuario() : "Anônimo").append("\n");
        sb.append("📄 Página: ").append(feedback.getPagina() != null ? feedback.getPagina() : "Não informada").append("\n\n");
        sb.append("🧩 Tipo: ").append(feedback.getTipo() != null ? feedback.getTipo().toUpperCase() : "N/A").append("\n");
        if (feedback.getRating() != null && feedback.getRating() > 0) {
            sb.append("⭐ Avaliação: ").append(feedback.getRating()).append(" / 5\n");
        }
        sb.append("\n💬 Mensagem:\n").append(feedback.getMensagem() != null ? feedback.getMensagem() : "Sem mensagem").append("\n");
        sb.append("\n====================================================\n");
        sb.append("Enviado automaticamente pelo PDFocus 🚀");
        return sb.toString();
    }
}
