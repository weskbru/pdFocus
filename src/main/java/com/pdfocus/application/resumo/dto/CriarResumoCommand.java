package com.pdfocus.application.resumo.dto;

import java.util.UUID;

public class CriarResumoCommand {

    private final UUID idUsuario;
    private final UUID idDisciplina;
    private final String titulo;
    private final String conteudo;

    public CriarResumoCommand(UUID idUsuario, UUID idDisciplina, String titulo, String conteudo) {
        this.idUsuario = idUsuario;
        this.idDisciplina = idDisciplina;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getTitulo() {
        return titulo;
    }

    public UUID getIdDisciplina() {
        return idDisciplina;
    }
}
