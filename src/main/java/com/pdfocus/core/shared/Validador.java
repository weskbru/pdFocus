package com.pdfocus.core.shared;

public class Validador {

    public static <T> T requireNotNull(T valor, String mensagem) {
        if (valor == null) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }

    public static String requireNotEmpty(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }
}
