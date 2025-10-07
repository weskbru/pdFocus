package com.pdfocus.core.models;

import com.pdfocus.core.shared.Validador;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Resumo {

    private final UUID id;
    private final UUID usuarioId;
    private final String titulo;
    private final String conteudo;
    private final Disciplina disciplina;
    private final UUID materialId;
    private final OffsetDateTime dataCriacao;

    private Resumo(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina, UUID materialId, OffsetDateTime dataCriacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.disciplina = disciplina;
        this.materialId = materialId;
        this.dataCriacao = dataCriacao;
    }

    public static Resumo criar(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina) {
        Validador.requireNotNull(id, "Id não pode ser nulo");
        Validador.requireNotNull(usuarioId, "Usuário responsável não pode ser nulo");
        Validador.requireNotEmpty(titulo, "Título é obrigatório");
        Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");


        return new Resumo(id, usuarioId, titulo, conteudo, disciplina, null, OffsetDateTime.now());
    }

    public static Resumo criarDeMaterial(UUID id, UUID usuarioId, String titulo, String conteudo,
                                         Disciplina disciplina, UUID materialId) {
        Validador.requireNotNull(id, "Id não pode ser nulo");
        Validador.requireNotNull(usuarioId, "Usuário responsável não pode ser nulo");
        Validador.requireNotEmpty(titulo, "Título é obrigatório");
        Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");
        Validador.requireNotNull(materialId, "Material não pode ser nulo");

        return new Resumo(id, usuarioId, titulo, conteudo, disciplina, materialId, OffsetDateTime.now());
    }

    // --- MÉTODOS DE ACESSO PÚBLICOS ---

    public UUID getId() {
        return id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public String getConteudo() {
        return conteudo;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public UUID getMaterialId() {
        return materialId;
    }

    public OffsetDateTime getDataCriacao() {
        return dataCriacao;
    }
}