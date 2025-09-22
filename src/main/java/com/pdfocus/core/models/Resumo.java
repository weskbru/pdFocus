package com.pdfocus.core.models;

import java.util.UUID;
import com.pdfocus.core.shared.Validador;

public class Resumo {

    private final UUID id;
    private final UUID usuarioId;
    private final String titulo;
    private final String conteudo;
    private final Disciplina disciplina;
    private final UUID materialId;

    private Resumo(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina, UUID materialId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.disciplina = disciplina;
        this.materialId = materialId; // CORREÇÃO: estava faltando esta linha!
    }

    public static Resumo criar(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina) {
        Validador.requireNotNull(id, "Id não pode ser nulo");
        Validador.requireNotNull(usuarioId, "Usuário responsável não pode ser nulo");
        Validador.requireNotEmpty(titulo, "Título é obrigatório");
        Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");

        return new Resumo(id, usuarioId, titulo, conteudo, disciplina, null);
    }

    // NOVO MÉTODO para resumos baseados em material (PDF)
    public static Resumo criarDeMaterial(UUID id, UUID usuarioId, String titulo, String conteudo,
                                         Disciplina disciplina, UUID materialId) {
        Validador.requireNotNull(id, "Id não pode ser nulo");
        Validador.requireNotNull(usuarioId, "Usuário responsável não pode ser nulo");
        Validador.requireNotEmpty(titulo, "Título é obrigatório");
        Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");
        Validador.requireNotNull(materialId, "Material não pode ser nulo"); // Nova validação

        return new Resumo(id, usuarioId, titulo, conteudo, disciplina, materialId);
    }

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

    // NOVO GETTER para o materialId
    public UUID getMaterialId() {
        return materialId;
    }
}