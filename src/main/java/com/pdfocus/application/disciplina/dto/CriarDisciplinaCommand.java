package com.pdfocus.application.disciplina.dto;

/**
 * Comando para carregar os dados necessários para criar uma nova disciplina.
 *
 * @param nome O nome da disciplina.
 * @param descricao A descrição da disciplina.
 */
public record CriarDisciplinaCommand(String nome, String descricao) {
}