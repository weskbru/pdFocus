package com.pdfocus.infra.controllers;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.dto.FeedbackResponse;
import com.pdfocus.application.feedback.port.entrada.EnviarFeedbackUseCase;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.UsuarioEntity; // <--- Importante
import com.pdfocus.infra.persistence.mapper.UsuarioMapper; // <--- Importante
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável por receber e registrar feedbacks de usuários.
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final EnviarFeedbackUseCase enviarFeedbackUseCase;
    private final UsuarioMapper usuarioMapper; // <--- Precisamos do Mapper para converter

    /**
     * Construtor com injeção de dependência.
     */
    public FeedbackController(EnviarFeedbackUseCase enviarFeedbackUseCase, UsuarioMapper usuarioMapper) {
        this.enviarFeedbackUseCase = enviarFeedbackUseCase;
        this.usuarioMapper = usuarioMapper;
    }

    /**
     * Recebe um feedback do usuário autenticado.
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> receberFeedback(
            @RequestBody @Valid FeedbackRequest request,
            // CORREÇÃO: Recebemos UsuarioEntity, pois é isso que o Spring Security carrega no UserDetails
            @AuthenticationPrincipal UsuarioEntity usuarioEntity
    ) {
        // 1. Convertemos a Entidade de Infraestrutura para o Modelo de Domínio
        // O usuarioEntity contém os dados do banco (incluindo contadores de limite)
        Usuario usuario = usuarioMapper.toDomain(usuarioEntity);

        // 2. Passamos o objeto de domínio para a regra de negócio
        Long feedbackId = enviarFeedbackUseCase.executar(request, usuario);

        // 3. Retornamos a resposta
        FeedbackResponse response = FeedbackResponse.sucesso(feedbackId, request.getTipo());
        return ResponseEntity.ok(response);
    }
}