package com.pdfocus.core.shared;

public class Validador {

    // Verifica se o valor é nulo. Se for, lança IllegalArgumentException com a mensagem fornecida.
    public static <T> T requireNotNull(T valor, String mensagem) {
        if (valor == null) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }

    // Verifica se a String é nula, vazia ou só com espaços em branco. Se for, lança IllegalArgumentException com a mensagem.
    public static String requireNotEmpty(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }
}
