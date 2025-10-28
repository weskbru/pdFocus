

package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Feedback;
import com.pdfocus.infra.persistence.entity.FeedbackEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o FeedbackMapper.
 * Segue o mesmo padrão dos outros mapper tests.
 */
@DisplayName("Testes para o FeedbackMapper")
class FeedbackMapperTest {

    @Test
    @DisplayName("Deve converter Feedback para FeedbackEntity")
    void deveConverterFeedbackParaFeedbackEntity() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now();
        Feedback feedback = new Feedback(
                1L,
                "BUG",
                4,
                "Mensagem de teste",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0...",
                dataCriacao
        );

        // Act
        FeedbackEntity entity = FeedbackMapper.toEntity(feedback);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("BUG", entity.getTipo());
        assertEquals(4, entity.getRating());
        assertEquals("Mensagem de teste", entity.getMensagem());
        assertEquals("usuario@exemplo.com", entity.getEmailUsuario());
        assertEquals("/dashboard", entity.getPagina());
        assertEquals("Mozilla/5.0...", entity.getUserAgent());
        assertEquals(dataCriacao, entity.getDataCriacao());
    }

    @Test
    @DisplayName("Deve converter FeedbackEntity para Feedback")
    void deveConverterFeedbackEntityParaFeedback() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now();
        FeedbackEntity entity = new FeedbackEntity(
                "BUG",
                4,
                "Mensagem de teste",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0...",
                dataCriacao
        );
        entity.setId(1L);

        // Act
        Feedback feedback = FeedbackMapper.toDomain(entity);

        // Assert
        assertNotNull(feedback);
        assertEquals(1L, feedback.getId());
        assertEquals("BUG", feedback.getTipo());
        assertEquals(4, feedback.getRating());
        assertEquals("Mensagem de teste", feedback.getMensagem());
        assertEquals("usuario@exemplo.com", feedback.getEmailUsuario());
        assertEquals("/dashboard", feedback.getPagina());
        assertEquals("Mozilla/5.0...", feedback.getUserAgent());
        assertEquals(dataCriacao, feedback.getDataCriacao());
    }

    @Test
    @DisplayName("Deve retornar null quando Feedback for null")
    void deveRetornarNullQuandoFeedbackForNull() {
        // Act
        FeedbackEntity entity = FeedbackMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve retornar null quando FeedbackEntity for null")
    void deveRetornarNullQuandoFeedbackEntityForNull() {
        // Act
        Feedback feedback = FeedbackMapper.toDomain(null);

        // Assert
        assertNull(feedback);
    }
}