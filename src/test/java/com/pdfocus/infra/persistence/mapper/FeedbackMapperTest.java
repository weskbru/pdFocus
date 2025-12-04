package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Feedback;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.FeedbackEntity;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes para o FeedbackMapper.
 * Ajustado para lidar com a injeção de dependência do UsuarioMapper e métodos de instância.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para o FeedbackMapper")
class FeedbackMapperTest {

    @Mock
    private UsuarioMapper usuarioMapper;

    private FeedbackMapper feedbackMapper;

    @BeforeEach
    void setUp() {
        // Instancia o mapper injetando o mock do UsuarioMapper
        feedbackMapper = new FeedbackMapper(usuarioMapper);
    }

    @Test
    @DisplayName("Deve converter Feedback para FeedbackEntity com Usuário")
    void deveConverterFeedbackParaFeedbackEntity() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now();
        Usuario usuario = new Usuario(UUID.randomUUID(), "Teste", "teste@email.com", "hash", true, 0, null, 0, null);

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
        feedback.setUsuario(usuario);

        UsuarioEntity usuarioEntityMock = new UsuarioEntity();
        usuarioEntityMock.setId(usuario.getId());

        // Configura o comportamento do mock
        when(usuarioMapper.toEntity(usuario)).thenReturn(usuarioEntityMock);

        // Act
        FeedbackEntity entity = feedbackMapper.toEntity(feedback);

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

        // Valida se o usuário foi mapeado corretamente
        assertNotNull(entity.getUsuario());
        assertEquals(usuario.getId(), entity.getUsuario().getId());

        // Verifica se o UsuarioMapper foi chamado
        verify(usuarioMapper).toEntity(usuario);
    }

    @Test
    @DisplayName("Deve converter FeedbackEntity para Feedback com Usuário")
    void deveConverterFeedbackEntityParaFeedback() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now();
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(UUID.randomUUID());

        FeedbackEntity entity = new FeedbackEntity(
                "BUG",
                4,
                "Mensagem de teste",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0...",
                dataCriacao,
                usuarioEntity
        );
        entity.setId(1L);

        Usuario usuarioMock = new Usuario(usuarioEntity.getId(), "Teste", "teste@email.com", "hash", true, 0, null, 0, null);

        // Configura o comportamento do mock
        when(usuarioMapper.toDomain(usuarioEntity)).thenReturn(usuarioMock);

        // Act
        Feedback feedback = feedbackMapper.toDomain(entity);

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

        // Valida se o usuário foi mapeado corretamente
        assertNotNull(feedback.getUsuario());
        assertEquals(usuarioEntity.getId(), feedback.getUsuario().getId());

        // Verifica se o UsuarioMapper foi chamado
        verify(usuarioMapper).toDomain(usuarioEntity);
    }

    @Test
    @DisplayName("Deve retornar null quando Feedback for null")
    void deveRetornarNullQuandoFeedbackForNull() {
        // Act
        FeedbackEntity entity = feedbackMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve retornar null quando FeedbackEntity for null")
    void deveRetornarNullQuandoFeedbackEntityForNull() {
        // Act
        Feedback feedback = feedbackMapper.toDomain(null);

        // Assert
        assertNull(feedback);
    }
}