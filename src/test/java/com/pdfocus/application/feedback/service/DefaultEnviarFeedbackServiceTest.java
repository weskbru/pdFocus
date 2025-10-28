package com.pdfocus.application.feedback.service;

import com.pdfocus.application.feedback.dto.FeedbackRequest;
import com.pdfocus.application.feedback.port.saida.FeedbackRepository;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.exceptions.EmailFeedbackException;
import com.pdfocus.core.exceptions.FeedbackInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes para o DefaultEnviarFeedbackService.
 * Segue o mesmo padrão dos outros service tests com Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para o DefaultEnviarFeedbackService")
class DefaultEnviarFeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private FeedbackEmailService feedbackEmailService;

    @InjectMocks
    private DefaultEnviarFeedbackService enviarFeedbackService;

    @Test
    @DisplayName("Deve enviar feedback com sucesso")
    void deveEnviarFeedbackComSucesso() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "BUG",
                4,
                "Encontrei um problema na funcionalidade X",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0..."
        );

        Feedback feedbackSalvo = new Feedback(
                1L,
                "BUG",
                4,
                "Encontrei um problema na funcionalidade X",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0...",
                java.time.LocalDateTime.now()
        );

        when(feedbackRepository.salvar(any(Feedback.class))).thenReturn(feedbackSalvo);
        doNothing().when(feedbackEmailService).enviarEmailFeedback(any(Feedback.class));

        // Act
        Long feedbackId = enviarFeedbackService.executar(request);

        // Assert
        assertNotNull(feedbackId);
        assertEquals(1L, feedbackId);
        verify(feedbackRepository, times(1)).salvar(any(Feedback.class));
        verify(feedbackEmailService, times(1)).enviarEmailFeedback(any(Feedback.class));
    }

    @Test
    @DisplayName("Deve enviar feedback sem email e sem rating")
    void deveEnviarFeedbackSemEmailESemRating() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "SUGGESTION",
                null,
                "Sugiro adicionar uma nova funcionalidade útil",
                null,
                "/disciplinas",
                "Mozilla/5.0..."
        );

        Feedback feedbackSalvo = new Feedback(
                2L,
                "SUGGESTION",
                null,
                "Sugiro adicionar uma nova funcionalidade útil",
                null,
                "/disciplinas",
                "Mozilla/5.0...",
                java.time.LocalDateTime.now()
        );

        when(feedbackRepository.salvar(any(Feedback.class))).thenReturn(feedbackSalvo);
        doNothing().when(feedbackEmailService).enviarEmailFeedback(any(Feedback.class));

        // Act
        Long feedbackId = enviarFeedbackService.executar(request);

        // Assert
        assertNotNull(feedbackId);
        assertEquals(2L, feedbackId);
        verify(feedbackRepository, times(1)).salvar(any(Feedback.class));
    }

    @Test
    @DisplayName("Deve continuar fluxo mesmo quando email falhar")
    void deveContinuarFluxoMesmoQuandoEmailFalhar() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "FEATURE",
                5,
                "Gostaria de ter uma nova funcionalidade",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0..."
        );

        Feedback feedbackSalvo = new Feedback(
                3L,
                "FEATURE",
                5,
                "Gostaria de ter uma nova funcionalidade",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0...",
                java.time.LocalDateTime.now()
        );

        when(feedbackRepository.salvar(any(Feedback.class))).thenReturn(feedbackSalvo);
        doThrow(new EmailFeedbackException("Falha no email")).when(feedbackEmailService).enviarEmailFeedback(any(Feedback.class));

        // Act & Assert
        assertThrows(EmailFeedbackException.class, () -> {
            enviarFeedbackService.executar(request);
        });

        verify(feedbackRepository, times(1)).salvar(any(Feedback.class));
        verify(feedbackEmailService, times(1)).enviarEmailFeedback(any(Feedback.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando request for inválido")
    void deveLancarExcecaoQuandoRequestForInvalido() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "BUG",
                4,
                "Curta", // Mensagem muito curta - deve falhar na validação
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Act & Assert
        assertThrows(FeedbackInvalidoException.class, () -> {
            enviarFeedbackService.executar(request);
        });

        verify(feedbackRepository, never()).salvar(any(Feedback.class));
        verify(feedbackEmailService, never()).enviarEmailFeedback(any(Feedback.class));
    }
}