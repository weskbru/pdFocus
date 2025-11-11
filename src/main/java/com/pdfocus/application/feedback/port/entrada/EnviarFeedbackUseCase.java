package com.pdfocus.application.feedback.port.entrada;

import com.pdfocus.application.feedback.dto.FeedbackRequest;

/**
 * Define o contrato (Porta de Entrada) para o caso de uso de envio de feedbacks.
 *
 * <p>Esta interface representa a entrada principal do fluxo de feedback na aplicação.
 * Sua responsabilidade é orquestrar o processo de validação, persistência e eventual
 * notificação (por e-mail, logs ou outros canais) de um feedback enviado pelo utilizador.</p>
 *
 * <p>O padrão segue o mesmo estilo dos demais casos de uso do projeto, como
 * {@code CriarDisciplinaUseCase} e {@code UploadMaterialUseCase}, adotando o método
 * {@code executar()} como ponto único de entrada.</p>
 *
 * <p>A implementação concreta deve:
 * <ul>
 *   <li>Validar o {@link FeedbackRequest} utilizando as regras de domínio;</li>
 *   <li>Converter o DTO em um objeto de domínio {@code Feedback};</li>
 *   <li>Persistir o feedback através da porta de saída correspondente;</li>
 *   <li>Acionar mecanismos de notificação ou auditoria, se aplicável.</li>
 * </ul></p>
 */
public interface EnviarFeedbackUseCase {

    /**
     * Executa o fluxo completo de envio de feedback, garantindo validação,
     * persistência e notificação do evento.
     *
     * <p>O método deve respeitar as regras de negócio do domínio de feedbacks,
     * assegurando que apenas dados válidos e coerentes sejam processados.</p>
     *
     * @param request o DTO contendo os dados do feedback
     * @return o identificador único (ID) do feedback persistido
     *
     * @throws com.pdfocus.core.exceptions.FeedbackInvalidoException se os dados forem inválidos
     * @throws com.pdfocus.core.exceptions.EmailFeedbackException se ocorrer erro no envio do e-mail
     */
    Long executar(FeedbackRequest request);
}
