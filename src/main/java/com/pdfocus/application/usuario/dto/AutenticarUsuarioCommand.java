package com.pdfocus.application.usuario.dto;

/**
 * DTO de comando (Command) que encapsula as credenciais de autenticação de um usuário.
 *
 * <p>Este objeto é usado como entrada para o caso de uso {@link com.pdfocus.application.usuario.port.entrada.AutenticarUsuarioUseCase}
 * para validar as credenciais e gerar um token JWT.</p>
 *
 * @param email O endereço de e-mail fornecido pelo usuário para login.
 * @param senha A senha em texto puro fornecida pelo usuário.
 */
public record AutenticarUsuarioCommand(String email, String senha) {
}
