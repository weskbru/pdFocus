package com.pdfocus.application.usuario.dto;

/**
 * DTO de comando (Command) que encapsula os dados necessários para registrar um novo usuário.
 *
 * <p>Este objeto é usado como entrada para o caso de uso {@link com.pdfocus.application.usuario.port.entrada.CadastrarUsuarioUseCase}.</p>
 *
 * @param nome  O nome completo do usuário.
 * @param email O endereço de e-mail do usuário, que também será usado para login.
 * @param senha A senha em texto puro escolhida pelo usuário (será criptografada antes de persistir).
 */
public record CadastrarUsuarioCommand(String nome, String email, String senha) {
}
