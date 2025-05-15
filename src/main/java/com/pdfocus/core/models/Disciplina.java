package com.pdfocus.core.models;

import java.util.UUID;

public class Disciplina {
    private final UUID id;
    private final String nome;
    private final String descricao;

    public Disciplina(UUID id, String nome, String descricao) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obrigatório");
        this.id = id;
        this.nome = nome;
        this.descricao = descricao; // pode ser null ou vazio, opcional
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
