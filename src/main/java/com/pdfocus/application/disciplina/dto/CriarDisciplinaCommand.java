package com.pdfocus.application.disciplina.dto;

/**
 * Comando (Command Object) usado como entrada no caso de uso de criação de uma nova disciplina.
 * <p>
 * Este objeto encapsula os dados necessários para a operação de criação,
 * seguindo o padrão de comandos da aplicação (input-bound data structure),
 * garantindo clareza e imutabilidade na comunicação entre camadas.
 *
 * @param nome      O nome da disciplina a ser criada.
 * @param descricao A descrição detalhada da disciplina.
 */
public record CriarDisciplinaCommand(String nome, String descricao) {
}
