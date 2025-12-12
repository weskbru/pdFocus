package com.pdfocus.application.feedback.service;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.port.entrada.EnviarFeedbackUseCase;
import com.pdfocus.application.feedback.port.saida.FeedbackEmailPort;
import com.pdfocus.application.feedback.port.saida.FeedbackRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.LimiteFeedbackExcedidoException;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.models.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class DefaultEnviarFeedbackService implements EnviarFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEmailPort feedbackEmailPort;
    private final UsuarioRepository usuarioRepository;

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

        // 1. Validações iniciais
        if (usuario == null) {
            System.out.println("❌ ERRO: O objeto 'usuario' chegou NULO no Service!");
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        } else {
            System.out.println("✅ Usuário recebido: " + usuario.getEmail());
        }

        System.out.println("📋 Validando Request: " + request);
        request.validar(); // Se falhar, solta exception aqui

        // 2. Validação de Limite
        System.out.println("⏳ Verificando limite diário...");
        validarLimiteDiario(usuario);
        System.out.println("✅ Limite diário: OK");

        // 3. Montagem e Salvamento
        System.out.println("💾 Salvando feedback...");

        // Convertendo DTO para Dominio e associando o usuário
        Feedback feedback = request.toDomain();
        feedback.setUsuario(usuario);

        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);
        System.out.println("✅ Feedback salvo com ID: " + feedbackSalvo.getId());

        // 4. Atualiza contador do usuário
        incrementarContadorFeedback(usuario);

        // 5. ENVIO DE E-MAIL (A PEÇA QUE FALTAVA) 🚀
        try {
            System.out.println("📨 [SERVICE] Chamando porta de e-mail...");
            feedbackEmailPort.enviarEmailFeedback(feedbackSalvo);
            System.out.println("✅ [SERVICE] Solicitação de envio realizada.");
        } catch (Exception e) {
            // Importante: O try-catch garante que se o e-mail falhar,
            // o feedback continua salvo no banco (não faz rollback).
            System.err.println("⚠️ [SERVICE] Erro ao tentar enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }

        return feedbackSalvo.getId();
    }

    private void validarLimiteDiario(Usuario usuario) {
        LocalDate hoje = LocalDate.now();
        int LIMITE = 2;

        if (usuario.getDataUltimoFeedback() == null || !hoje.equals(usuario.getDataUltimoFeedback())) {
            usuario.setFeedbacksHoje(0);
            usuario.setDataUltimoFeedback(hoje);
        }

        if (usuario.getFeedbacksHoje() >= LIMITE) {
            throw new LimiteFeedbackExcedidoException(
                    "Você atingiu o limite de " + LIMITE + " feedbacks por dia. Volte amanhã! 🚀"
            );
        }
    }

    private void incrementarContadorFeedback(Usuario usuario) {
        usuario.setFeedbacksHoje(usuario.getFeedbacksHoje() + 1);
        usuarioRepository.salvar(usuario);
    }
}