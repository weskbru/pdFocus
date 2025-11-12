package com.pdfocus.application.resumo.port.saida;

/**
 * Porta de saída para serviços de resumo automático de texto.
 *
 * <p>Define o contrato que diferentes implementações de resumo devem seguir,
 * permitindo que o módulo de aplicação utilize qualquer mecanismo de resumo
 * (algoritmo local, API externa, modelo de IA, etc.) sem depender da implementação concreta.</p>
 *
 * <p>Essa abstração facilita testes, manutenção e troca de tecnologia no futuro.</p>
 */
public interface ResumidorIAPort {

    /**
     * Gera um resumo a partir do texto completo fornecido.
     *
     * <p>O resumo deve respeitar o limite de palavras especificado e
     * manter a essência e os pontos principais do conteúdo original.</p>
     *
     * @param textoCompleto Texto original a ser resumido. Não deve ser nulo ou vazio.
     * @param maxPalavras   Número máximo de palavras permitidas no resumo.
     * @return Uma {@link String} contendo o texto resumido.
     */
    String resumir(String textoCompleto, int maxPalavras);
}
