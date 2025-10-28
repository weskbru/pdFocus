package com.pdfocus.application.feedback.service;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.port.entrada.EnviarFeedbackUseCase;
import com.pdfocus.application.feedback.port.saida.FeedbackRepository;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.FeedbackInvalidoException;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do caso de uso para envio de feedback.
 * Segue o mesmo padrão dos outros Default Services com separação de concerns.
 *
 * Responsabilidade: Orquestrar o fluxo completo - validação, persistência e notificação.
 */
@Service
@Transactional
public class DefaultEnviarFeedbackService implements EnviarFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEmailService feedbackEmailService;

    /**
     * Construtor com injeção de dependências seguindo o padrão do projeto.
     * Separado para facilitar testes e evolução futura.
     */
    public DefaultEnviarFeedbackService(
            FeedbackRepository feedbackRepository,
            FeedbackEmailService feedbackEmailService) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackEmailService = feedbackEmailService;
    }

    /**
     * Executa o fluxo completo de envio de feedback.
     * Segue o padrão dos outros use cases com validação → domínio → persistência → notificação.
     *
     * @param request DTO com os dados do feedback
     * @return ID do feedback persistido
     * @throws FeedbackInvalidoException se os dados forem inválidos
     * @throws EmailFeedbackException se houver falha no envio do email
     */
    @Override
    public Long executar(FeedbackRequest request) {
        // 1. Validar dados de entrada (seguindo padrão dos outros services)
        request.validar();

        // 2. Criar entidade de domínio (conversão DTO → Domain)
        Feedback feedback = criarFeedbackFromRequest(request);

        // 3. Persistir no repositório
        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);

        // 4. Enviar notificação por email (usando service separado)
        feedbackEmailService.enviarEmailFeedback(feedbackSalvo);

        // 5. Retornar ID do feedback persistido
        return feedbackSalvo.getId();
    }

    /**
     * Converte o DTO de request para entidade de domínio.
     * Segue o padrão de métodos privados auxiliares dos outros services.
     */
    private Feedback criarFeedbackFromRequest(FeedbackRequest request) {
        return new Feedback(
                request.getTipo(),
                request.getRating(),
                request.getMensagem(),
                request.getEmailUsuario(),
                request.getPagina(),
                request.getUserAgent()
        );
    }
}