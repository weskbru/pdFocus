package com.pdfocus.core.shared;

import com.pdfocus.core.exceptions.*;

public class Validador {

    public static <T> T requireNotNull(T valor, String mensagem) {
        if (valor == null) throw new CampoNuloException(mensagem);
        return valor;
    }

    public static String requireNotEmpty(String valor, String mensagem) {
        if (valor == null) throw new CampoNuloException(mensagem);
        if (valor.trim().isEmpty()) throw new CampoVazioException(mensagem);
        return valor;
    }


}
