package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import java.util.UUID;

/**
 * Representa uma <strong>disciplina de estudo</strong> pertencente a um usuário.
 * <p>
 * Garante sua própria consistência interna, validando invariantes de domínio
 * através de exceções personalizadas. Cada disciplina pertence exatamente a um
 * usuário e possui nome obrigatório e descrição opcional.
 * </p>
 *
 * @see com.pdfocus.infra.persistence.entity.DisciplinaEntity
 */
public class Disciplina {

    /** Identificador único da disciplina. */
    private final UUID id;

    /** Identificador do usuário dono da disciplina. */
    private final UUID usuarioId;

    /** Nome da disciplina (obrigatório). */
    private String nome;

    /** Descrição textual opcional. */
    private String descricao;

    /**
     * Cria uma nova {@code Disciplina} garantindo um estado inicial válido.
     *
     * @param id         Identificador único.
     * @param nome       Nome da disciplina.
     * @param descricao  Descrição (opcional).
     * @param usuarioId  Identificador do usuário dono.
     * @throws CampoNuloException se {@code id}, {@code usuarioId} ou {@code nome} forem nulos.
     * @throws CampoVazioException se {@code nome} estiver em branco.
     */
    public Disciplina(UUID id, String nome, String descricao, UUID usuarioId) {
        if (id == null) throw new CampoNuloException("ID da disciplina não pode ser nulo");
        if (usuarioId == null) throw new CampoNuloException("ID do usuário não pode ser nulo");

        this.id = id;
        this.usuarioId = usuarioId;

        // Usa os setters para aplicar as mesmas regras de validação.
        this.setNome(nome);
        this.setDescricao(descricao);
    }

    // --- Getters ---

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    // --- Setters com validação de domínio ---

    /**
     * Atualiza o nome da disciplina.
     *
     * @param nome Novo nome.
     * @throws CampoNuloException se for {@code null}.
     * @throws CampoVazioException se estiver em branco.
     */
    public void setNome(String nome) {
        if (nome == null) throw new CampoNuloException("Nome não pode ser nulo");
        if (nome.isBlank()) throw new CampoVazioException("Nome não pode estar em branco");
        this.nome = nome;
    }

    /**
     * Atualiza a descrição da disciplina.
     *
     * @param descricao Novo texto descritivo (opcional).
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
