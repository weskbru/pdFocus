package com.pdfocus.application.feedback.service;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.port.entrada.EnviarFeedbackUseCase;
import com.pdfocus.application.feedback.port.saida.FeedbackEmailPort;
import com.pdfocus.application.feedback.port.saida.FeedbackRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository; // <--- NOVO IMPORT
import com.pdfocus.core.exceptions.LimiteFeedbackExcedidoException;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.FeedbackInvalidoException;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import com.pdfocus.core.models.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class DefaultEnviarFeedbackService implements EnviarFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEmailPort feedbackEmailPort;
    private final UsuarioRepository usuarioRepository; // <--- Dependência Nova

    // Atualize o construtor para incluir o UsuarioRepository
    public DefaultEnviarFeedbackService(
            FeedbackRepository feedbackRepository,
            FeedbackEmailPort feedbackEmailPort,
            UsuarioRepository usuarioRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackEmailPort = feedbackEmailPort;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Long executar(FeedbackRequest request, Usuario usuario) {
        System.out.println("---- DEBUG INICIADO ----");

        // 1. Verificar se o Usuário chegou
        if (usuario == null) {
            System.out.println("❌ ERRO: O objeto 'usuario' chegou NULO no Service!");
            // Isso causaria NullPointerException logo abaixo
        } else {
            System.out.println("✅ Usuário recebido: " + usuario.getEmail());
        }

        // 2. Verificar o Request
        System.out.println("📋 Validando Request: " + request);
        try {
            request.validar();
            System.out.println("✅ Validação do Request: SUCESSO");
        } catch (Exception e) {
            System.out.println("❌ ERRO NA VALIDAÇÃO: " + e.getMessage());
            throw e; // Re-lança para o controller pegar
        }

        // 3. Validação de Limite
        System.out.println("⏳ Verificando limite diário...");
        validarLimiteDiario(usuario);
        System.out.println("✅ Limite diário: OK");

        // ... resto do código

        System.out.println("💾 Salvando feedback...");
        Feedback feedback = request.toDomain();
        feedback.setUsuario(usuario);

        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);
        System.out.println("✅ Feedback salvo com ID: " + feedbackSalvo.getId());

        incrementarContadorFeedback(usuario);

        // ... envio de email

        return feedbackSalvo.getId();
    }

    /**
     * Verifica o limite olhando para o estado do usuário.
     * Realiza o "Lazy Reset" se o dia tiver mudado.
     */
    private void validarLimiteDiario(Usuario usuario) {
        LocalDate hoje = LocalDate.now();
        int LIMITE = 2; // Limite de 2 feedbacks por dia

        // Se a data do último feedback não for hoje, reseta o contador
        if (usuario.getDataUltimoFeedback() == null || !hoje.equals(usuario.getDataUltimoFeedback())) {
            usuario.setFeedbacksHoje(0);
            usuario.setDataUltimoFeedback(hoje);
            // O save será chamado no final do fluxo, ou podemos salvar aqui se preferir
        }

        if (usuario.getFeedbacksHoje() >= LIMITE) {
            throw new LimiteFeedbackExcedidoException(
                    "Você atingiu o limite de " + LIMITE + " feedbacks por dia. Agradecemos o apoio! Volte amanhã. 🚀"
            );
        }
    }

    /**
     * Incrementa o contador e persiste o usuário atualizado.
     */
    private void incrementarContadorFeedback(Usuario usuario) {
        usuario.setFeedbacksHoje(usuario.getFeedbacksHoje() + 1);
        usuarioRepository.salvar(usuario);
    }

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