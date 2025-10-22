package com.pdfocus.application.resumo.port.saida;

/**
 * Porta para serviços de resumo automático de texto.
 * Define o contrato que diferentes implementações de resumo devem seguir.
 *
 * Esta interface permite que troquemos a implementação do resumo
 * (algoritmo local, API externa, etc.) sem mudar o código que a usa.
 */
public interface ResumidorIAPort {

    /**
     * Gera um resumo do texto fornecido.
     *
     * @param textoCompleto Texto original a ser resumido
     * @param maxPalavras Número máximo de palavras no resumo
     * @return Texto resumido
     */
    String resumir(String textoCompleto, int maxPalavras);
}