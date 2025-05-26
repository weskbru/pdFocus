package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;

import java.util.UUID;

/**
 * Representa uma disciplina de estudo.
 */
public class Disciplina {
    private final UUID id;
    private final String nome;
    private final String descricao;

    /**
     * Construtor para criar uma nova instância de {@code Disciplina}.
     *
     * @param id        O identificador único da disciplina. Não pode ser nulo.
     * @param nome      O nome da disciplina. Não pode ser nulo ou em branco.
     * @param descricao Uma descrição opcional para a disciplina.
     * @throws CampoNuloException Se o {@code id} ou o {@code nome} forem nulos.
     * @throws CampoVazioException Se o {@code nome} estiver em branco.
     */
    public Disciplina(UUID id, String nome, String descricao) {
        if (id == null) throw new CampoNuloException("ID não pode ser nulo");
        if (nome == null) throw new CampoNuloException("Nome não pode ser nulo");
        if (nome.isBlank()) throw new CampoVazioException("Nome não pode estar em branco");
        this.id = id;
        this.nome = nome;
        this.descricao = descricao; // pode ser null ou vazio, opcional
    }

    /**
     * Obtém o identificador único da disciplina.
     *
     * @return O ID da disciplina.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Obtém o nome da disciplina.
     *
     * @return O nome da disciplina.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Obtém a descrição da disciplina.
     *
     * @return A descrição da disciplina. Pode ser nulo.
     */
    public String getDescricao() {
        return descricao;
    }
}