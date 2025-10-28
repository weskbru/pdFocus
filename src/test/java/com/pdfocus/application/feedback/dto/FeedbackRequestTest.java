package com.pdfocus.application.feedback.dto;

import com.pdfocus.core.exceptions.ValorInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o DTO FeedbackRequest.
 */
@DisplayName("Testes para o DTO FeedbackRequest")
class FeedbackRequestTest {

    @Test
    @DisplayName("Deve validar request com dados válidos")
    void deveValidarRequestComDadosValidos() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "BUG",
                4,
                "Mensagem com mais de 10 caracteres",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Act & Assert
        assertDoesNotThrow(request::validar);
    }

    @Test
    @DisplayName("Deve validar request sem email e sem rating")
    void deveValidarRequestSemEmailESemRating() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "SUGGESTION",
                null,
                "Mensagem com mais de 10 caracteres",
                null,
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Act & Assert
        assertDoesNotThrow(request::validar);
    }

    @Test
    @DisplayName("Deve lançar exceção quando mensagem for muito curta")
    void deveLancarExcecaoQuandoMensagemForMuitoCurta() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "BUG",
                4,
                "Curta",
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Act & Assert
        assertThrows(ValorInvalidoException.class, request::validar);
    }

    @Test
    @DisplayName("Deve lançar exceção quando mensagem for muito longa")
    void deveLancarExcecaoQuandoMensagemForMuitoLonga() {
        // Arrange
        String mensagemLonga = "a".repeat(1001);
        FeedbackRequest request = new FeedbackRequest(
                "BUG",
                4,
                mensagemLonga,
                "usuario@exemplo.com",
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Act & Assert
        assertThrows(ValorInvalidoException.class, request::validar);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email for inválido")
    void deveLancarExcecaoQuandoEmailForInvalido() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "BUG",
                4,
                "Mensagem com mais de 10 caracteres",
                "email-invalido",
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Act & Assert
        assertThrows(ValorInvalidoException.class, request::validar);
    }

    @Test
    @DisplayName("Deve aceitar email nulo")
    void deveAceitarEmailNulo() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(
                "BUG",
                4,
                "Mensagem com mais de 10 caracteres",
                null,
                "/dashboard",
                "Mozilla/5.0..."
        );

        // Act & Assert
        assertDoesNotThrow(request::validar);
    }
}