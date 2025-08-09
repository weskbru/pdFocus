package com.pdfocus.application.usuario.dto;

/**
 * Comando contendo as credenciais para a autenticação de um usuário.
 *
 * @param email O e-mail fornecido pelo usuário.
 * @param senha A senha em texto puro fornecida pelo usuário.
 */
public record AutenticarUsuarioCommand(String email, String senha) {
}
