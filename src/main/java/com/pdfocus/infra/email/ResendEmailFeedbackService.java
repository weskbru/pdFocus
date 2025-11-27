package com.pdfocus.infra.email;

import com.pdfocus.application.feedback.port.saida.FeedbackEmailPort;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço responsável pelo envio de feedbacks via e-mail utilizando a API do Resend.
 * Versão atualizada com formatação HTML e tratamento de dados.
 */
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
        mostrarNoConsole(feedback); // Log útil para dev

        Map<String, Object> body = new HashMap<>();
        body.put("from", "PDFocus Test <onboarding@resend.dev>");
        body.put("to", new String[]{emailDestino});
        body.put("subject", "📨 Novo Feedback recebido - PDFocus");

        // MUDANÇA PRINCIPAL: Usamos "html" em vez de "text"
        body.put("html", montarHtmlFeedback(feedback));

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
            System.out.println("✅ Feedback enviado para Resend com status: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar feedback via Resend: " + e.getMessage());
            throw new EmailFeedbackException("Erro ao enviar feedback via Resend", e);
        }
    }

    private void mostrarNoConsole(Feedback feedback) {
        // Mantive seu log original, é ótimo para debug rápido
        System.out.println("\n🎯 ================= FEEDBACK RECEBIDO =================");
        System.out.println("📧 TIPO: " + (feedback.getTipo() != null ? feedback.getTipo() : "Não especificado"));
        System.out.println("👤 USUÁRIO: " + (feedback.getEmailUsuario() != null ? feedback.getEmailUsuario() : "Anônimo"));
        System.out.println("========================================================\n");
    }

    /**
     * Monta o corpo do e-mail em HTML profissional.
     */
    private String montarHtmlFeedback(Feedback feedback) {
        // 1. Formatar a Data (Ex: 26/11/2025 às 14:30)
        String dataFormatada = "Data desconhecida";
        if (feedback.getDataCriacao() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            dataFormatada = feedback.getDataCriacao().format(formatter);
        }

        // 2. Traduzir o Tipo (Supondo que seja um Enum ou String em inglês)
        String tipoRaw = feedback.getTipo() != null ? feedback.getTipo().toString() : "OTHER";
        String tipoTraduzido = switch (tipoRaw) {
            case "SUGGESTION" -> "💡 Sugestão";
            case "BUG" -> "🐛 Erro / Bug";
            case "COMPLIMENT" -> "❤️ Elogio";
            case "OTHER" -> "📝 Outro";
            default -> "📝 " + tipoRaw;
        };

        // 3. Definir cor do badge baseada no tipo (Opcional, mas fica chique)
        String corBorda = tipoRaw.equals("BUG") ? "#E74C3C" : "#2E86C1"; // Vermelho para bug, Azul para resto

        // 4. Montar o HTML usando Text Block (Java 15+)
        return """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; max-width: 600px; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;">
                
                <div style="background-color: %s; color: white; padding: 20px; text-align: center;">
                    <h2 style="margin: 0;">Novo Feedback Recebido</h2>
                </div>

                <div style="padding: 20px;">
                    <p style="margin-bottom: 5px;"><strong>👤 Usuário:</strong> %s</p>
                    <p style="margin-bottom: 5px;"><strong>📅 Data:</strong> %s</p>
                    <p style="margin-bottom: 20px;"><strong>📄 Página:</strong> <a href="%s" style="color: %s;">%s</a></p>
                    
                    <div style="background-color: #f8f9fa; border-left: 5px solid %s; padding: 15px; border-radius: 4px;">
                        <p style="margin: 0; font-size: 14px; text-transform: uppercase; color: #777;">Tipo de Feedback</p>
                        <p style="margin: 5px 0 0 0; font-size: 18px; font-weight: bold;">%s</p>
                        
                        <hr style="border: 0; border-top: 1px solid #ddd; margin: 10px 0;">
                        
                        <p style="margin: 0; font-size: 14px; text-transform: uppercase; color: #777;">Avaliação</p>
                        <p style="margin: 5px 0 0 0; font-size: 18px;">%s / 5 ⭐</p>
                    </div>

                    <h3 style="margin-top: 25px; color: #444;">Mensagem:</h3>
                    <div style="background-color: #fff; border: 1px solid #ddd; padding: 15px; border-radius: 4px; font-style: italic; color: #555;">
                        "%s"
                    </div>
                </div>

                <div style="background-color: #f1f1f1; padding: 15px; text-align: center; font-size: 12px; color: #888;">
                    Enviado automaticamente pelo sistema <strong>PDFocus</strong> 🚀
                </div>
            </div>
            """.formatted(
                corBorda, // Cor do cabeçalho
                feedback.getEmailUsuario() != null ? feedback.getEmailUsuario() : "Anônimo",
                dataFormatada,
                feedback.getPagina(), corBorda, feedback.getPagina(), // Link da página
                corBorda, // Cor da borda lateral
                tipoTraduzido,
                feedback.getRating() != null ? feedback.getRating() : "N/A",
                feedback.getMensagem() != null ? feedback.getMensagem().replace("\n", "<br>") : "Sem mensagem" // Troca quebra de linha por <br>
        );
    }
}