package com.pdfocus.application.usuario.dto;

/**
 * DTO que representa a resposta de uma autenticação bem-sucedida.
 *
 * @param token O token JWT gerado para o usuário.
 */
public record AuthenticationResponse(String token) {
}
