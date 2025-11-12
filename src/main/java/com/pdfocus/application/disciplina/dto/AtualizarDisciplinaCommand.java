package com.pdfocus.application.disciplina.dto;

/**
 * Comando (Command Object) usado como entrada no caso de uso de atualização de uma disciplina existente.
 * <p>
 * Este objeto encapsula os novos valores que serão aplicados à entidade {@code Disciplina},
 * garantindo imutabilidade e clareza na comunicação entre a camada de aplicação e o domínio.
 *
 * @param nome      O novo nome da disciplina.
 * @param descricao A nova descrição da disciplina.
 */
public record AtualizarDisciplinaCommand(String nome, String descricao) {
}
