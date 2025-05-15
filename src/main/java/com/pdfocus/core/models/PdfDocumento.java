package com.pdfocus.core.models;

import java.util.UUID;

import static com.pdfocus.core.shared.Validador.requireNotEmpty;
import static com.pdfocus.core.shared.Validador.requireNotNull;

public class PdfDocumento {
    private final UUID id;
    private final String nome;
    private final String caminho;
    private final Disciplina disciplina;

    public PdfDocumento(UUID id, String nome, String caminho, Disciplina disciplina) {
        this.id = requireNotNull(id, "ID não pode ser nulo");
        this.nome = requireNotEmpty(nome, "Nome do PDF é obrigatório");
        this.caminho = requireNotEmpty(caminho, "Caminho do PDF é obrigatório");
        this.disciplina = requireNotNull(disciplina, "Disciplina não pode ser nula");
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCaminho() {
        return caminho;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }
}
