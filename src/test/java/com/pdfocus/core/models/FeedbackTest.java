package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.CampoVazioException;
import com.pdfocus.core.exceptions.ValorInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a entidade de domínio Feedback.
 * Segue o mesmo padrão dos outros testes de domínio (DisciplinaTest, etc).
 */
@DisplayName("Testes para a entidade Feedback")
class FeedbackTest {

    @Test
    @DisplayName("Deve criar feedback com dados válidos")
    void deveCriarFeedbackComDadosValidos() {
        // Arrange & Act
        Feedback feedback = new Feedback(
                "BUG",
                4,
                "Encontrei um problema na funcionalidade X",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Assert
        assertNotNull(feedback);
        assertEquals("BUG", feedback.getTipo());
        assertEquals(4, feedback.getRating());
        assertEquals("Encontrei um problema na funcionalidade X", feedback.getMensagem());
        assertEquals("usuario@exemplo.com", feedback.getEmailUsuario());
        assertEquals("/dashboard", feedback.getPagina());
        assertEquals("Mozilla/5.0...", feedback.getUserAgent());
        assertNotNull(feedback.getDataCriacao());
    }

    @Test
    @DisplayName("Deve criar feedback sem email e sem rating")
    void deveCriarFeedbackSemEmailESemRating() {
        // Arrange & Act
        Feedback feedback = new Feedback(
                "SUGGESTION",
                null,
                "Sugiro adicionar uma nova funcionalidade",
                null,
                "/disciplinas",
                "Mozilla/5.0..."
        );

        // Assert
        assertNotNull(feedback);
        assertEquals("SUGGESTION", feedback.getTipo());
        assertNull(feedback.getRating());
        assertNull(feedback.getEmailUsuario());
    }

    @Test
    @DisplayName("Deve converter tipo para uppercase")
    void deveConverterTipoParaUppercase() {
        // Arrange & Act
        Feedback feedback = new Feedback(
                "suggestion",
                5,
                "Mensagem de teste",
                null,
                "/pagina",
                "user-agent"
        );

        // Assert
        assertEquals("SUGGESTION", feedback.getTipo());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\t\n"})
    @DisplayName("Deve lançar exceção quando tipo for vazio")
    void deveLancarExcecaoQuandoTipoForVazio(String tipoInvalido) {
        // Arrange & Act & Assert
        assertThrows(CampoVazioException.class, () ->
                new Feedback(
                        tipoInvalido,
                        3,
                        "Mensagem válida",
                        "email@exemplo.com",
                        "/pagina",
                        "user-agent"
                )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando mensagem for vazia")
    void deveLancarExcecaoQuandoMensagemForVazia() {
        // Arrange & Act & Assert
        assertThrows(CampoVazioException.class, () ->
                new Feedback(
                        "BUG",
                        3,
                        "",
                        "email@exemplo.com",
                        "/pagina",
                        "user-agent"
                )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo for inválido")
    void deveLancarExcecaoQuandoTipoForInvalido() {
        // Arrange & Act & Assert
        assertThrows(ValorInvalidoException.class, () ->
                new Feedback(
                        "TIPO_INVALIDO",
                        3,
                        "Mensagem válida",
                        "email@exemplo.com",
                        "/pagina",
                        "user-agent"
                )
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6, -1, 10})
    @DisplayName("Deve lançar exceção quando rating for inválido")
    void deveLancarExcecaoQuandoRatingForInvalido(int ratingInvalido) {
        // Arrange & Act & Assert
        assertThrows(ValorInvalidoException.class, () ->
                new Feedback(
                        "BUG",
                        ratingInvalido,
                        "Mensagem válida",
                        "email@exemplo.com",
                        "/pagina",
                        "user-agent"
                )
        );
    }

    @Test
    @DisplayName("Deve aceitar rating nulo")
    void deveAceitarRatingNulo() {
        // Arrange & Act
        Feedback feedback = new Feedback(
                "FEATURE",
                null,
                "Mensagem válida",
                "email@exemplo.com",
                "/pagina",
                "user-agent"
        );

        // Assert
        assertNotNull(feedback);
        assertNull(feedback.getRating());
    }

    @Test
    @DisplayName("Deve reconstituir feedback do banco com construtor completo")
    void deveReconstituirFeedbackDoBanco() {
        // Arrange
        var dataCriacao = java.time.LocalDateTime.now().minusDays(1);

        // Act
        Feedback feedback = new Feedback(
                1L,
                "BUG",
                4,
                "Mensagem do banco",
                "email@exemplo.com",
                "/pagina",
                "user-agent",
                dataCriacao
        );

        // Assert
        assertEquals(1L, feedback.getId());
        assertEquals("BUG", feedback.getTipo());
        assertEquals(4, feedback.getRating());
        assertEquals(dataCriacao, feedback.getDataCriacao());
    }
}