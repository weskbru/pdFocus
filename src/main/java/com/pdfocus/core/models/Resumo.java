package com.pdfocus.core.models;

import com.pdfocus.core.shared.Validador;

import java.time.LocalDateTime;
import java.util.UUID;


public class Resumo {

    private final UUID id;
    private final UUID usuarioId;
    private final String titulo;
    private final String conteudo;
    private final Disciplina disciplina;

    public Resumo(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina) {
        this.id = Validador.requireNotNull(id, "ID não pode ser nulo");
        this.usuarioId = Validador.requireNotNull(usuarioId, "Usuario responsavel não pode ser nulo");
        this.titulo = Validador.requireNotEmpty(titulo, "Título é obrigatório");
        this.conteudo = Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        this.disciplina = Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");
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
}
