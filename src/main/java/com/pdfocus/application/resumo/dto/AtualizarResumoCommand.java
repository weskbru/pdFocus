package com.pdfocus.application.resumo.dto;

/**
 * Comando para carregar os dados necessários para atualizar um resumo.
 *
 * @param titulo   O novo título para o resumo.
 * @param conteudo O novo conteúdo para o resumo.
 */
public record AtualizarResumoCommand(String titulo, String conteudo) {

}
