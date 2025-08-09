package com.pdfocus.application.disciplina.dto;

/**
 * Comando para carregar os dados necessários para atualizar uma disciplina.
 *
 * @param nome O novo nome para a disciplina.
 * @param descricao A nova descrição para a disciplina.
 */
public record AtualizarDisciplinaCommand(String nome, String descricao) {
}
