package com.pdfocus.core.exceptions.resumo;

import java.util.UUID;

public class TextoNaoPodeSerExtraidoException extends RuntimeException {

    public TextoNaoPodeSerExtraidoException(UUID materialId) {
        super("Não foi possível extrair texto do material com ID: " + materialId);
    }

    public TextoNaoPodeSerExtraidoException(String materialId, Throwable cause) {
        super("Não foi possível extrair texto do material com ID: " + materialId, cause);
    }

    public TextoNaoPodeSerExtraidoException(String s) {
    }
}