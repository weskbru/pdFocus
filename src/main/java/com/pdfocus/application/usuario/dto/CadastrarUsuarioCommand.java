package com.pdfocus.application.usuario.dto;

/**
 * Comando contendo os dados necessários para registrar um novo usuário.
 *
 * @param nome O nome completo do usuário.
 * @param email O e-mail do usuário, que será usado para login.
 * @param senha A senha em texto puro escolhida pelo usuário.
 */
public record CadastrarUsuarioCommand(String nome, String email, String senha) {
}
