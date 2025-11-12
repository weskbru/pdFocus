package com.pdfocus.infra.controllers;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.dto.FeedbackResponse;
import com.pdfocus.application.feedback.port.entrada.EnviarFeedbackUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável por receber e registrar feedbacks de usuários.
 * <p>
 * Atua como ponto de entrada HTTP para o caso de uso {@link EnviarFeedbackUseCase},
 * garantindo isolamento da lógica de negócio e padronização de resposta.
 * </p>
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final EnviarFeedbackUseCase enviarFeedbackUseCase;

    /**
     * Construtor com injeção de dependência, seguindo o padrão de inicialização
     * adotado em toda a camada de controladores.
     *
     * @param enviarFeedbackUseCase Caso de uso responsável pelo processamento e persistência do feedback.
     */
    public FeedbackController(EnviarFeedbackUseCase enviarFeedbackUseCase) {
        this.enviarFeedbackUseCase = enviarFeedbackUseCase;
    }

    /**
     * Recebe um feedback do usuário autenticado e o encaminha para processamento.
     * <p>
     * Este endpoint é usado tanto para sugestões quanto para relatórios de erro.
     * Retorna uma resposta padronizada confirmando o recebimento.
     * </p>
     *
     * @param request DTO contendo os dados do feedback (tipo, mensagem, etc.).
     * @return 200 (OK) com {@link FeedbackResponse} indicando sucesso na operação.
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> receberFeedback(@RequestBody FeedbackRequest request) {
        // Executa o caso de uso de envio de feedback
        Long feedbackId = enviarFeedbackUseCase.executar(request);

        // Retorna resposta padronizada com o ID e o tipo de feedback
        FeedbackResponse response = FeedbackResponse.sucesso(feedbackId, request.getTipo());

        return ResponseEntity.ok(response);
    }
}
