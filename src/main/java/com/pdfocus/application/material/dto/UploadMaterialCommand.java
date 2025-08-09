package com.pdfocus.application.material.dto;

import java.io.InputStream;
import java.util.UUID;

/**
 * Comando para carregar os dados necessários para o upload de um novo material.
 *
 * @param nomeOriginal O nome original do ficheiro (ex: "apostila.pdf").
 * @param tipoArquivo O tipo MIME do ficheiro (ex: "application/pdf").
 * @param tamanho O tamanho do ficheiro em bytes.
 * @param disciplinaId O ID da disciplina à qual este material será associado.
 * @param inputStream O conteúdo do ficheiro como um fluxo de bytes.
 */
public record UploadMaterialCommand(
        String nomeOriginal,
        String tipoArquivo,
        long tamanho,
        UUID disciplinaId,
        InputStream inputStream
) {
}
