package com.pdfocus.application.usuario.port.saida;

public interface AuthEmailPort {
    void enviarEmailConfirmacao(String emailDestino, String nomeUsuario, String linkConfirmacao);
    // Futuramente: void enviarRecuperacaoSenha(...);
}