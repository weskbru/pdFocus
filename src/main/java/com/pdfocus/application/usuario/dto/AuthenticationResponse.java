package com.pdfocus.application.usuario.dto;

/**
 * DTO responsável por enviar os dados de autenticação de volta para o cliente.
 * Atualizado para incluir nome e e-mail, permitindo que o Frontend exiba "Olá, [Nome]"
 * imediatamente após o login ou confirmação.
 */
public class AuthenticationResponse {

    private String token;
    private String nome;
    private String email;

    // 1. Construtor Vazio (Necessário para serialização JSON/Jackson)
    public AuthenticationResponse() {
    }

    // 2. Construtor Completo (O que o seu Service está tentando usar agora)
    public AuthenticationResponse(String token, String nome, String email) {
        this.token = token;
        this.nome = nome;
        this.email = email;
    }

    // 3. Construtor Antigo (Apenas Token) - Mantido para evitar quebra de testes antigos
    public AuthenticationResponse(String token) {
        this.token = token;
    }

    // --- Getters e Setters ---

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}