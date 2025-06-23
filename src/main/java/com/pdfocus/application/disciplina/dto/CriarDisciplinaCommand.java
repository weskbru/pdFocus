package com.pdfocus.application.disciplina.dto;

/**
 * DTO (Data Transfer Object) para o comando de criação de uma nova {@link com.pdfocus.core.models.Disciplina}.
 * Contém os dados necessários para solicitar a criação de uma disciplina.
 */
public class CriarDisciplinaCommand {

    private String nome;
    private String descricao;

    /**
     * Construtor para criar uma instância de {@code CriarDisciplinaCommand}.
     *
     * @param nome      O nome da disciplina a ser criada.
     * @param descricao A descrição da disciplina a ser criada.
     */
    public CriarDisciplinaCommand(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Obtém o nome da disciplina a ser criada.
     *
     * @return O nome da disciplina.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Obtém a descrição da disciplina a ser criada.
     *
     * @return A descrição da disciplina.
     */
    public String getDescricao() {
        return descricao;
    }
}