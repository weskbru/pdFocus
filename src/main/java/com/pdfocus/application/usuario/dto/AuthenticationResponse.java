package com.pdfocus.application.usuario.dto;

/**
 * DTO (Data Transfer Object) que representa a resposta de uma autenticação bem-sucedida.
 *
 * <p>Este objeto é retornado pelo caso de uso {@link com.pdfocus.application.usuario.port.entrada.AutenticarUsuarioUseCase}
 * e contém o token JWT que deve ser usado em requisições subsequentes para autenticação.</p>
 *
 * @param token O token JWT gerado para o usuário, que autentica suas requisições.
 */
public record AuthenticationResponse(String token) {
}
