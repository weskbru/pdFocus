package com.pdfocus.application.feedback.service;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.port.entrada.EnviarFeedbackUseCase;
import com.pdfocus.application.feedback.port.saida.FeedbackEmailPort;
import com.pdfocus.application.feedback.port.saida.FeedbackRepository;
import com.pdfocus.core.exceptions.LimiteFeedbackExcedidoException;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.FeedbackInvalidoException;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Implementação padrão ({@code Default}) do caso de uso {@link EnviarFeedbackUseCase}.
 *
 * <p>Esta classe orquestra o fluxo completo de envio de feedbacks no sistema,
 * garantindo que cada etapa (validação, persistência e notificação) seja executada
 * de forma segura, transacional e desacoplada das tecnologias subjacentes.</p>
 *
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Validar o {@link FeedbackRequest} recebido, conforme as regras de negócio;</li>
 *   <li>Converter o DTO em um objeto de domínio {@link Feedback};</li>
 *   <li>Persistir o feedback através da porta de saída {@link FeedbackRepository};</li>
 *   <li>Acionar a notificação via e-mail através da porta {@link FeedbackEmailPort};</li>
 *   <li>Garantir atomicidade via controle transacional do Spring.</li>
 * </ul>
 *
 * <p>Em caso de falha na notificação, é lançada uma {@link EmailFeedbackException},
 * sem comprometer a integridade dos dados persistidos.</p>
 */
@Service
@Transactional
public class DefaultEnviarFeedbackService implements EnviarFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEmailPort feedbackEmailPort;

    /**
     * Construtor de injeção de dependências. As portas de saída são
     * providas pela camada de infraestrutura (adapters).
     *
     * @param feedbackRepository Porta de saída responsável pela persistência de feedbacks.
     * @param feedbackEmailPort Porta de saída responsável pelo envio de notificações por e-mail.
     */
    public DefaultEnviarFeedbackService(
            FeedbackRepository feedbackRepository,
            FeedbackEmailPort feedbackEmailPort) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackEmailPort = feedbackEmailPort;
    }

    /**
     * Executa o fluxo completo de envio de feedback.
     *
     * <p>Etapas do fluxo:</p>
     * <ol>
     *   <li>Validação do {@link FeedbackRequest};</li>
     *   <li>Conversão para objeto de domínio {@link Feedback};</li>
     *   <li>Persistência do feedback no repositório;</li>
     *   <li>Envio de notificação por e-mail (assíncrona ou síncrona).</li>
     * </ol>
     *
     * @param request DTO contendo os dados do feedback submetido.
     * @return ID do feedback persistido.
     * @throws FeedbackInvalidoException se os dados forem inválidos.
     * @throws EmailFeedbackException se houver falha no envio do e-mail de notificação.
     */
    @Override
    public Long executar(FeedbackRequest request) {
        // 1. Validação de formato (dados obrigatórios)
        request.validar();

        // 2. [CORREÇÃO AQUI] Validação de Regra de Negócio: Limite Diário
        // Sem essa linha, a regra que criamos nunca é executada!
        validarLimiteDiario(request.getEmailUsuario());

        // 3. Conversão para domínio puro
        Feedback feedback = criarFeedbackFromRequest(request);

        // 4. Persistência
        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);

        // 5. Notificação via e-mail (tratando falhas)
        try {
            feedbackEmailPort.enviarEmailFeedback(feedbackSalvo);
        } catch (Exception e) {
            throw new EmailFeedbackException("Erro ao enviar e-mail de feedback", e);
        }

        return feedbackSalvo.getId();
    }

    /**
     * Verifica se o usuário já enviou mais de 2 feedbacks hoje.
     */
    private void validarLimiteDiario(String email) {
        // Se não tem e-mail ou é anônimo, ignoramos a regra (ou bloqueamos por IP futuramente)
        if (email == null || email.isBlank() || email.equalsIgnoreCase("Anônimo")) {
            return;
        }

        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay(); // Hoje 00:00
        LocalDateTime fimDoDia = LocalDate.now().atTime(LocalTime.MAX); // Hoje 23:59:59

        long quantidadeHoje = feedbackRepository.contarPorEmailEPeriodo(email, inicioDoDia, fimDoDia);

        if (quantidadeHoje >= 1) {
            throw new LimiteFeedbackExcedidoException(
                    "Você atingiu o limite de 2 feedbacks por dia. Agradecemos o apoio! Volte amanhã. 🚀"
            );
        }
    }

    /**
     * Constrói a entidade de domínio {@link Feedback} a partir do DTO de entrada.
     * Este método isola a transformação de dados, mantendo o serviço principal
     * focado na orquestração de regras de negócio.
     *
     * @param request o DTO contendo os dados recebidos do cliente.
     * @return uma nova instância de {@link Feedback} pronta para persistência.
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
