package com.pdfocus.infra.email;

import com.pdfocus.application.usuario.port.saida.AuthEmailPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResendAuthEmailService implements AuthEmailPort {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String remetente;

    public ResendAuthEmailService(
            RestTemplate restTemplate,
            @Value("${app.resend.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.remetente = "PDFocus Security <suporte@pdfocus.com.br>";

        // --- DEBUG CRÍTICO ---
        // Vamos imprimir no log as primeiras letras da chave para conferir se o Java pegou a certa
        if (apiKey != null && apiKey.length() > 5) {
            System.out.println("🔑 [DEBUG] ResendAuthService iniciou com chave: " + apiKey.substring(0, 5) + "...");
        } else {
            System.err.println("❌ [DEBUG] ResendAuthService iniciou SEM CHAVE ou chave inválida!");
        }
    }

    @Override
    public void enviarEmailConfirmacao(String emailDestino, String nomeUsuario, String linkConfirmacao) {
        // Log antes de tentar enviar
        System.out.println("🚀 [DEBUG] Tentando enviar e-mail para: " + emailDestino);

        Map<String, Object> body = new HashMap<>();
        body.put("from", remetente);
        body.put("to", new String[]{emailDestino});
        body.put("subject", "🚀 Bem-vindo ao PDFocus! Confirme sua conta");
        body.put("html", montarHtmlConfirmacao(nomeUsuario, linkConfirmacao));

        enviarParaResend(body);
    }

    private void enviarParaResend(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity("https://api.resend.com/emails", request, String.class);
            System.out.println("✅ E-mail de autenticação enviado com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro CRÍTICO ao enviar e-mail Auth: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String montarHtmlConfirmacao(String nome, String link) {
        return """
            <div style="font-family: sans-serif; padding: 20px; border: 1px solid #ddd;">
                <h2>Olá %s!</h2>
                <p>Confirme seu email: <a href="%s">Clicar aqui</a></p>
            </div>
            """.formatted(nome, link);
    }
}