package com.pdfocus.application.feedback.port.entrada;

import com.pdfocus.application.feedback.dto.FeedbackRequest;

/**
 * Porta de entrada (Use Case) para o envio de feedback.
 * Segue o mesmo padrão das outras portas de entrada (CriarDisciplinaUseCase, etc).
 *
 * Responsabilidade: Orquestrar o fluxo de envio de feedback - validação, persistência e notificação.
 */
public interface EnviarFeedbackUseCase {

    /**
     * Executa o fluxo completo de envio de feedback.
     * Segue o padrão de nomenclatura dos outros use cases (executar).
     *
     * @param request DTO com os dados do feedback
     * @return ID do feedback persistido
     * @throws com.pdfocus.core.exceptions.FeedbackInvalidoException se os dados forem inválidos
     * @throws com.pdfocus.core.exceptions.EmailFeedbackException se houver falha no envio do email
     */
    Long executar(FeedbackRequest request);
}