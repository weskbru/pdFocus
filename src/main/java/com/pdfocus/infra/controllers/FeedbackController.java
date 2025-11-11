package com.pdfocus.infra.controllers;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.dto.FeedbackResponse;
import com.pdfocus.application.feedback.port.entrada.EnviarFeedbackUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operações relacionadas a feedback.
 * Segue o mesmo padrão dos outros controllers (AuthController, DisciplinaController, etc).
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final EnviarFeedbackUseCase enviarFeedbackUseCase;

    /**
     * Construtor com injeção de dependência seguindo o padrão do projeto.
     */
    public FeedbackController(EnviarFeedbackUseCase enviarFeedbackUseCase) {
        this.enviarFeedbackUseCase = enviarFeedbackUseCase;
    }

    /**
     * Endpoint para receber feedback dos usuários.
     * Segue o padrão POST com retorno 200 e DTO de resposta.
     *
     * @param request DTO com os dados do feedback
     * @return Resposta 200 (OK) com confirmação do recebimento
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> receberFeedback(@RequestBody FeedbackRequest request) {
        // O @Valid foi removido pois estamos usando validação manual no Request
        // Executa o use case e obtém o ID do feedback persistido
        Long feedbackId = enviarFeedbackUseCase.executar(request);

        // Cria resposta de sucesso seguindo o padrão dos outros endpoints
        FeedbackResponse response = FeedbackResponse.sucesso(feedbackId, request.getTipo());

        return ResponseEntity.ok(response);
    }
}