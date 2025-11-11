package com.pdfocus.application.feedback.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO (Data Transfer Object) para representar a resposta de confirmação de um feedback.
 * Segue o mesmo padrão do MaterialRecenteResponse com método de fábrica fromDomain.
 */
public record FeedbackResponse(
        Long id,
        String tipo,
        String mensagemStatus,
        String dataEnvioFormatada
) {

    // Formatador de data seguindo o mesmo padrão do MaterialRecenteResponse
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    /**
     * Método de fábrica para criar resposta de sucesso.
     * Usado pelo Controller para respostas imediatas.
     */
    public static FeedbackResponse sucesso(Long feedbackId, String tipo) {
        String dataFormatada = LocalDateTime.now().format(FORMATADOR_DATA);
        String mensagemStatus = gerarMensagemStatus(tipo);

        return new FeedbackResponse(
                feedbackId,
                tipo,
                mensagemStatus,
                dataFormatada
        );
    }

    /**
     * Método de fábrica para converter um objeto de domínio Feedback para este DTO.
     * Útil para endpoints que retornam feedbacks salvos.
     */
    public static FeedbackResponse fromDomain(com.pdfocus.core.models.Feedback feedback) {
        String dataFormatada = feedback.getDataCriacao() != null
                ? feedback.getDataCriacao().format(FORMATADOR_DATA)
                : "-";

        String mensagemStatus = gerarMensagemStatus(feedback.getTipo());

        return new FeedbackResponse(
                feedback.getId(),
                feedback.getTipo(),
                mensagemStatus,
                dataFormatada
        );
    }

    /**
     * Método auxiliar privado para gerar a mensagem de status baseada no tipo de feedback.
     */
    private static String gerarMensagemStatus(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "BUG" -> "Bug reportado com sucesso. Iremos investigar!";
            case "SUGGESTION" -> "Sugestão recebida. Obrigado pela contribuição!";
            case "FEATURE" -> "Ideia de feature anotada. Adoramos a sugestão!";
            case "OTHER" -> "Feedback recebido com sucesso. Obrigado!";
            default -> "Feedback processado com sucesso.";
        };
    }
}