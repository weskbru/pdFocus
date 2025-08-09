package com.pdfocus.core.models;

import com.pdfocus.core.shared.Validador;

import java.util.UUID;

/**
 * Representa um material de estudo (um arquivo) no domínio da aplicação.
 * Contém as informações (metadata) sobre o arquivo.
 */
public class Material {

    private final UUID id;
    private final String nomeOriginal;
    private final String nomeStorage; // Nome único do arquivo no sistema de armazenamento
    private final String tipoArquivo; // Ex: "application/pdf"
    private final long tamanho; // Tamanho em bytes
    private final UUID usuarioId;
    private final UUID disciplinaId; // A qual disciplina este material pertence

    private Material(UUID id, String nomeOriginal, String nomeStorage, String tipoArquivo, long tamanho, UUID usuarioId, UUID disciplinaId) {
        this.id = id;
        this.nomeOriginal = nomeOriginal;
        this.nomeStorage = nomeStorage;
        this.tipoArquivo = tipoArquivo;
        this.tamanho = tamanho;
        this.usuarioId = usuarioId;
        this.disciplinaId = disciplinaId;
    }

    /**
     * Metodo de fábrica para criar uma nova instância de Material.
     * Garante que um material só pode ser criado em um estado válido.
     */
    public static Material criar(UUID id, String nomeOriginal, String nomeStorage, String tipoArquivo, long tamanho, UUID usuarioId, UUID disciplinaId) {
        Validador.requireNotNull(id, "ID do material não pode ser nulo");
        Validador.requireNotEmpty(nomeOriginal, "Nome original do arquivo é obrigatório");
        Validador.requireNotEmpty(nomeStorage, "Nome de armazenamento do arquivo é obrigatório");
        Validador.requireNotEmpty(tipoArquivo, "Tipo do arquivo é obrigatório");
        Validador.requireNotNull(usuarioId, "ID do usuário não pode ser nulo");
        Validador.requireNotNull(disciplinaId, "ID da disciplina não pode ser nulo");
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho do arquivo deve ser maior que zero.");
        }

        return new Material(id, nomeOriginal, nomeStorage, tipoArquivo, tamanho, usuarioId, disciplinaId);
    }

    // Getters
    public UUID getId() { return id; }
    public String getNomeOriginal() { return nomeOriginal; }
    public String getNomeStorage() { return nomeStorage; }
    public String getTipoArquivo() { return tipoArquivo; }
    public long getTamanho() { return tamanho; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getDisciplinaId() { return disciplinaId; }
}
