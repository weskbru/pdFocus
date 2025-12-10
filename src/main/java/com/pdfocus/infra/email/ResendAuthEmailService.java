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
            @Value("${app.resend.api-key}") String apiKeyInjetada) {
        this.restTemplate = restTemplate;

        // --- 🛠️ FIX HARDCODED: Usando a chave que funcionou no PowerShell ---
        // Assim garantimos que o Java use a chave correta, ignorando problemas de variável de ambiente
        this.apiKey = "re_c2xt2Tqx_Kyt2LYyhoDjt8y6Pdn3wioD8";

        this.remetente = "PDFocus Security <suporte@pdfocus.com.br>";

        // Log para confirmar no Render que a alteração subiu
        System.out.println("🔑 [DEBUG MARRETA] Usando chave FIXA: " + this.apiKey);
    }

    @Override
    public void enviarEmailConfirmacao(String emailDestino, String nomeUsuario, String linkConfirmacao) {
        System.out.println("🚀 Iniciando envio para: " + emailDestino);

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
            System.out.println("✅ SUCESSO! E-mail de autenticação enviado.");
        } catch (Exception e) {
            System.err.println("❌ ERRO ao enviar e-mail Auth: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String montarHtmlConfirmacao(String nome, String link) {
        return """
            <div style="font-family: sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;">
                <h2 style="color: #ea580c; text-align: center;">Bem-vindo ao PDFocus! 🎓</h2>
                <p>Olá, <strong>%s</strong>!</p>
                <p>Obrigado por criar sua conta. Clique abaixo para confirmar:</p>
                <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #ea580c; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;">Confirmar E-mail</a>
                </div>
                <p style="font-size: 12px; color: #666;">Link: %s</p>
            </div>
            """.formatted(nome, link, link);
    }
}