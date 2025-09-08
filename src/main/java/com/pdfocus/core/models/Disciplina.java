package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;

import java.util.UUID;

/**
 * Representa uma disciplina de estudo. Este é um objeto de domínio rico,
 * responsável por garantir sua própria consistência (invariantes) através
 * de validações rigorosas no construtor e nos setters, utilizando
 * exceções personalizadas para diagnósticos precisos.
 */
public class Disciplina {

    // Atributos imutáveis: definidos no "nascimento" e nunca mais alterados.
    private final UUID id;
    private final UUID usuarioId;

    // Atributos mutáveis: podem ser alterados durante o ciclo de vida do objeto.
    private String nome;
    private String descricao;

    /**
     * Construtor para criar uma nova instância de {@code Disciplina}.
     * Garante o estado inicial válido do objeto.
     */
    public Disciplina(UUID id, String nome, String descricao, UUID usuarioId) {
        if (id == null) throw new CampoNuloException("ID da disciplina não pode ser nulo");
        if (usuarioId == null) throw new CampoNuloException("ID do usuário não pode ser nulo");

        this.id = id;
        this.usuarioId = usuarioId;

        // Delega a validação dos campos mutáveis para os setters,
        // garantindo que a lógica de validação exista em um único lugar (DRY).
        this.setNome(nome);
        this.setDescricao(descricao);
    }

    // --- GETTERS ---
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public UUID getUsuarioId() { return usuarioId; }

    // --- SETTERS COM VALIDAÇÃO PERSONALIZADA ---

    /**
     * Atualiza o nome da disciplina, garantindo que o novo nome é válido.
     * @param nome O novo nome para a disciplina.
     * @throws CampoNuloException se o nome for nulo.
     * @throws CampoVazioException se o nome estiver em branco.
     */
    public void setNome(String nome) {
        if (nome == null) throw new CampoNuloException("Nome não pode ser nulo");
        if (nome.isBlank()) throw new CampoVazioException("Nome não pode estar em branco");
        this.nome = nome;
    }

    /**
     * Atualiza a descrição da disciplina.
     * @param descricao A nova descrição. Pode ser nula ou vazia.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

