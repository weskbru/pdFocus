package com.pdfocus.application.resumo.port.saida;

/**
 * Porta de saída para extração de texto de arquivos (PDFs, documentos, etc.).
 * Esta interface define o contrato para diferentes implementações de extração de texto.
 */
public interface TextExtractorPort {

    /**
     * Extrai texto de um arquivo no sistema de armazenamento.
     *
     * @param nomeStorage O nome único do arquivo no sistema de armazenamento
     * @return O texto extraído do arquivo
     * @throws TextoNaoPodeSerExtraidoException se ocorrer erro na extração
     */
    String extrairTexto(String nomeStorage);
}