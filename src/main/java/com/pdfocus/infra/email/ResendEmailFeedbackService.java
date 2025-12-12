package com.pdfocus.infra.email;

import com.pdfocus.application.feedback.port.saida.FeedbackEmailPort;
import com.pdfocus.core.models.Feedback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
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
    @Async
    public void enviarEmailFeedback(Feedback feedback) {
        System.out.println("💬 [FEEDBACK] Iniciando envio assíncrono...");

        Map<String, Object> body = new HashMap<>();
        body.put("from", "PDFocus Feedback <suporte@pdfocus.com.br>");
        body.put("to", new String[]{emailDestino});
        body.put("subject", "📨 Novo Feedback recebido - PDFocus");
        body.put("html", montarHtmlFeedback(feedback));

        enviarParaResend(body);
    }

    private void enviarParaResend(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity("https://api.resend.com/emails", request, String.class);
            System.out.println("✅ [FEEDBACK] E-mail enviado com sucesso!");
        } catch (Exception e) {
            // Em método @Async, não lançamos exceção para cima, apenas logamos
            System.err.println("❌ [FEEDBACK] Erro ao enviar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String montarHtmlFeedback(Feedback feedback) {
        // Formatação de data segura
        String dataFormatada = "Data desconhecida";
        if (feedback.getDataCriacao() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            dataFormatada = feedback.getDataCriacao().format(formatter);
        }

        // Definição de cores e tipos
        String tipo = feedback.getTipo() != null ? feedback.getTipo().toUpperCase() : "OUTROS";
        String corBorda = "BUG".equals(tipo) ? "#E74C3C" : "#2E86C1"; // Vermelho se for Bug

        // <--- CORREÇÃO 3: Acesso correto aos dados do usuário (getUsuario().getNome())
        String nomeUsuario = feedback.getUsuario() != null ? feedback.getUsuario().getNome() : "Anônimo";
        String emailUsuario = feedback.getUsuario() != null ? feedback.getUsuario().getEmail() : "Sem e-mail";

        return """
            <div style="font-family: sans-serif; max-width: 600px; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;">
                <div style="background-color: %s; color: white; padding: 20px; text-align: center;">
                    <h2 style="margin: 0;">Novo Feedback Recebido</h2>
                </div>
                <div style="padding: 20px;">
                    <p><strong>👤 Usuário:</strong> %s (%s)</p>
                    <p><strong>📅 Data:</strong> %s</p>
                    
                    <div style="background-color: #f8f9fa; border-left: 5px solid %s; padding: 15px; margin: 20px 0;">
                        <p style="margin: 0; font-size: 12px; color: #777;">TIPO</p>
                        <p style="margin: 0; font-size: 18px; font-weight: bold;">%s</p>
                    </div>

                    <h3 style="color: #444;">Mensagem:</h3>
                    <div style="background-color: #fff; border: 1px solid #ddd; padding: 15px; border-radius: 4px; font-style: italic;">
                        "%s"
                    </div>
                </div>
                <div style="background-color: #f1f1f1; padding: 10px; text-align: center; font-size: 12px; color: #888;">
                    Sistema PDFocus 🚀
                </div>
            </div>
            """.formatted(
                corBorda,
                nomeUsuario, emailUsuario,
                dataFormatada,
                corBorda,
                tipo,
                feedback.getMensagem() != null ? feedback.getMensagem().replace("\n", "<br>") : "Sem mensagem"
        );
    }
}