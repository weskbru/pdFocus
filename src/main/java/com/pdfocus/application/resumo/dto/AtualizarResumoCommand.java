package com.pdfocus.application.resumo.dto;

/**
 * Comando (DTO) utilizado para transferir os dados necessários
 * para atualizar um resumo existente.
 *
 * <p>Contém apenas os campos que podem ser alterados: título e conteúdo.</p>
 *
 * @param titulo   O novo título do resumo. Não deve ser nulo nem vazio.
 * @param conteudo O novo conteúdo do resumo. Pode conter texto longo.
 */
public record AtualizarResumoCommand(String titulo, String conteudo) {

}
