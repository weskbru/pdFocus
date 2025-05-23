package com.pdfocus.core.models;


import java.util.UUID;
import com.pdfocus.core.shared.Validador;


public class Resumo {

    private final UUID id;
    private final UUID usuarioId;
    private final String titulo;
    private final String conteudo;
    private final Disciplina disciplina;

    private Resumo(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.disciplina = disciplina;
    }

    public static Resumo criar(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina) {
        Validador.requireNotNull(id, "Id não pode ser nulo");
        Validador.requireNotNull(usuarioId, "Usuário responsável não pode ser nulo");
        Validador.requireNotEmpty(titulo, "Título é obrigatório");
        Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");

        return new Resumo(id, usuarioId, titulo, conteudo, disciplina);
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
