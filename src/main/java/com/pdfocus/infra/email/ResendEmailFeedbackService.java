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
        System.out.println("💬 [FEEDBACK] Preparando HTML visual...");

        Map<String, Object> body = new HashMap<>();
        // Ajuste o remetente conforme sua validação no Resend
        body.put("from", "PDFocus Feedback <suporte@pdfocus.com.br>");
        body.put("to", new String[]{emailDestino});

        String tipoEmoji = "BUG".equals(feedback.getTipo().toString()) ? "🐛" : "💡";
        body.put("subject", tipoEmoji + " Novo Feedback: " + feedback.getTipo());

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
            System.out.println("✅ [FEEDBACK] E-mail visual enviado!");
        } catch (Exception e) {
            System.err.println("❌ [FEEDBACK] Erro no envio: " + e.getMessage());
        }
    }

    private String montarHtmlFeedback(Feedback feedback) {
        // 1. Formata a Data
        String dataFormatada = "Data desconhecida";
        if (feedback.getDataCriacao() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            dataFormatada = feedback.getDataCriacao().format(formatter);
        }

        // 2. Lógica de Cores
        String tipoRaw = feedback.getTipo() != null ? feedback.getTipo().toString() : "OTHER";
        boolean isBug = "BUG".equalsIgnoreCase(tipoRaw);
        String corDestaque = isBug ? "#D32F2F" : "#2563EB"; // Vermelho ou Azul

        String tipoTexto = switch (tipoRaw) {
            case "BUG" -> "🐛 Erro / Bug";
            case "SUGGESTION" -> "💡 Sugestão";
            case "COMPLIMENT" -> "❤️ Elogio";
            default -> "📝 " + tipoRaw;
        };

        // 3. Dados Importantes (E-mail e Página)
        String usuarioEmail = feedback.getUsuario() != null ? feedback.getUsuario().getEmail() : "Anônimo";
        String linkPagina = feedback.getPagina() != null ? feedback.getPagina() : "#";

        // HTML Completo
        return """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; background-color: #ffffff;">
                
                <div style="background-color: %s; color: white; padding: 20px; text-align: center;">
                    <h2 style="margin: 0; font-size: 22px; font-weight: 600;">Novo Feedback Recebido</h2>
                </div>

                <div style="padding: 25px;">
                    <p style="margin: 8px 0; color: #333;">
                        👤 <strong>Usuário:</strong> <a href="mailto:%s" style="color: #2563eb; text-decoration: none;">%s</a>
                    </p>
                    
                    <p style="margin: 8px 0; color: #333;">
                        📅 <strong>Data:</strong> %s
                    </p>
                    
                    <p style="margin: 8px 0; color: #333;">
                        📄 <strong>Página:</strong> <a href="%s" target="_blank" style="color: %s; text-decoration: none;">%s</a>
                    </p>

                    <div style="background-color: #f8f9fa; border-left: 5px solid %s; padding: 20px; margin-top: 25px; border-radius: 4px;">
                        
                        <p style="margin: 0; font-size: 11px; color: #888; text-transform: uppercase; letter-spacing: 1px;">TIPO DE FEEDBACK</p>
                        <p style="margin: 5px 0 15px 0; font-size: 20px; font-weight: bold; color: #333;">
                            %s
                        </p>
                        
                        <hr style="border: 0; border-top: 1px solid #e0e0e0; margin: 15px 0;">
                        
                        <p style="margin: 0; font-size: 11px; color: #888; text-transform: uppercase; letter-spacing: 1px;">AVALIAÇÃO</p>
                        <p style="margin: 5px 0 0 0; font-size: 20px; font-weight: bold; color: #333;">
                            %s / 5 ⭐
                        </p>
                    </div>

                    <h3 style="margin-top: 30px; font-size: 16px; color: #333; margin-bottom: 10px;">Mensagem:</h3>
                    <div style="border: 1px solid #e0e0e0; padding: 15px; border-radius: 6px; color: #555; font-style: italic; background-color: #fff;">
                        "%s"
                    </div>
                </div>

                <div style="background-color: #f1f1f1; padding: 15px; text-align: center; font-size: 12px; color: #888; border-top: 1px solid #e0e0e0;">
                    Enviado automaticamente pelo sistema <strong>PDFocus</strong> 🚀
                </div>
            </div>
            """.formatted(
                corDestaque, // Cor do Header
                usuarioEmail, usuarioEmail, // Link mailto e Texto do E-mail
                dataFormatada,
                linkPagina, corDestaque, linkPagina, // Link da Página (href e texto)
                corDestaque, // Cor da borda lateral
                tipoTexto, // Tipo (Bug/Sugestão)
                feedback.getRating(), // Nota (Avaliação)
                feedback.getMensagem() != null ? feedback.getMensagem().replace("\n", "<br>") : "Sem mensagem"
        );
    }
}