package com.pdfocus.application.resumo.port.saida;

import com.pdfocus.core.exceptions.resumo.TextoNaoPodeSerExtraidoException;

/**
 * Porta de saída para serviços de extração de texto a partir de arquivos
 * (PDFs, documentos de texto, etc.).
 *
 * <p>Define o contrato que diferentes implementações de extração devem seguir,
 * permitindo que o módulo de aplicação utilize qualquer mecanismo de extração
 * sem depender da implementação concreta (local, API externa, biblioteca de terceiros, etc.).</p>
 */
public interface TextExtractorPort {

    /**
     * Extrai o texto contido em um arquivo armazenado no sistema.
     *
     * <p>O método deve retornar o texto completo do arquivo, preservando
     * a ordem e o conteúdo legível. Em caso de falha na extração, lança
     * a exceção {@link TextoNaoPodeSerExtraidoException}.</p>
     *
     * @param nomeStorage O nome único do arquivo no sistema de armazenamento.
     * @return Uma {@link String} contendo o texto extraído do arquivo.
     * @throws TextoNaoPodeSerExtraidoException Se ocorrer erro na extração do texto.
     */
    String extrairTexto(String nomeStorage);
}
