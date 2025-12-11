package com.pdfocus.core.models;

import java.time.LocalDateTime;

public class ConfirmationToken {
    private String token;
    private LocalDateTime criadoEm;
    private LocalDateTime expiraEm;
    private LocalDateTime dataConfirmacao;
    private Usuario usuario;

    public ConfirmationToken(String token, LocalDateTime criadoEm, LocalDateTime expiraEm, LocalDateTime dataConfirmacao, Usuario usuario) {
        this.token = token;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
        this.dataConfirmacao = dataConfirmacao;
        this.usuario = usuario;
    }

    // Getters e Setters
    public String getToken() { return token; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getExpiraEm() { return expiraEm; }
    public LocalDateTime getDataConfirmacao() { return dataConfirmacao; }
    public Usuario getUsuario() { return usuario; }
}