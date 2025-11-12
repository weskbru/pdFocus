package com.pdfocus.application.feedback.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa a resposta de confirmação do envio de um feedback.
 *
 * <p>Este DTO é utilizado para devolver ao cliente uma resposta amigável e padronizada
 * após o envio de um feedback, incluindo o identificador do feedback, seu tipo,
 * uma mensagem contextual e a data de criação formatada.</p>
 *
 * <p>Segue o mesmo padrão adotado em {@code MaterialRecenteResponse}, com métodos
 * de fábrica para conversão direta a partir do domínio e para respostas imediatas.</p>
 *
 * <p>Esta classe é imutável, implementada como um {@link java.lang.Record} para
 * garantir segurança e simplicidade na serialização JSON.</p>
 */
public record FeedbackResponse(
        Long id,
        String tipo,
        String mensagemStatus,
        String dataEnvioFormatada
) {

    /**
     * Formatador de data padrão para exibição amigável no frontend.
     * Exemplo de saída: "07/11/2025 às 21:30".
     */
    private static final DateTimeFormatter FORMATADOR_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    /**
     * Cria uma instância de {@link FeedbackResponse} representando
     * uma resposta de sucesso imediatamente após o envio do feedback.
     *
     * <p>Usado principalmente pelo Controller após o processamento
     * bem-sucedido de um feedback enviado pelo utilizador.</p>
     *
     * @param feedbackId identificador do feedback recém-criado
     * @param tipo tipo do feedback (BUG, SUGGESTION, FEATURE ou OTHER)
     * @return uma nova instância de {@link FeedbackResponse} com data e mensagem formatadas
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
     * Converte uma entidade de domínio {@link com.pdfocus.core.models.Feedback}
     * para seu equivalente de resposta ({@code FeedbackResponse}).
     *
     * <p>Usado por endpoints que retornam feedbacks persistidos, garantindo
     * consistência na formatação de data e mensagem de status.</p>
     *
     * @param feedback objeto de domínio {@link com.pdfocus.core.models.Feedback}
     * @return uma nova instância de {@link FeedbackResponse} com dados formatados
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
     * Gera uma mensagem de status contextualizada com base no tipo de feedback recebido.
     *
     * <p>Essa mensagem é mostrada ao utilizador como confirmação textual,
     * mantendo a linguagem natural e empática.</p>
     *
     * @param tipo tipo do feedback (BUG, SUGGESTION, FEATURE ou OTHER)
     * @return mensagem descritiva associada ao tipo do feedback
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
