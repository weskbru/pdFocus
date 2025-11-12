package com.pdfocus.application.material.dto;

import java.io.InputStream;
import java.util.UUID;

/**
 * Comando de entrada para o caso de uso de upload de material didático.
 *
 * <p>
 * Este objeto transporta todos os dados necessários para a criação de um novo
 * {@code Material}, incluindo os metadados (nome, tipo, tamanho) e o conteúdo
 * binário do ficheiro em si.
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b></p>
 * <ul>
 *   <li>Pertence à camada de <b>aplicação</b>, e não contém lógica de negócio.</li>
 *   <li>É consumido por {@link com.pdfocus.application.material.service.DefaultUploadMaterialService}.</li>
 *   <li>Serve como uma fronteira clara entre o controlador REST e o domínio.</li>
 * </ul>
 *
 * @param nomeOriginal Nome original do arquivo enviado (ex: {@code "apostila.pdf"}).
 * @param tipoArquivo Tipo MIME do arquivo (ex: {@code "application/pdf"}).
 * @param tamanho Tamanho do arquivo em bytes.
 * @param disciplinaId Identificador da disciplina à qual o material será associado.
 * @param inputStream Conteúdo do arquivo como um fluxo de bytes para leitura.
 */
public record UploadMaterialCommand(
        String nomeOriginal,
        String tipoArquivo,
        long tamanho,
        UUID disciplinaId,
        InputStream inputStream
) {
}
