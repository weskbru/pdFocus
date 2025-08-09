package com.pdfocus.application.material.port.entrada;

import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.core.models.Material;

import java.util.UUID;

/**
 * Caso de uso para realizar o upload de um novo material de estudo.
 */
public interface UploadMaterialUseCase {

    /**
     * Executa a operação de upload de um material.
     *
     * @param command O comando contendo os dados e o conteúdo do ficheiro.
     * @param usuarioId O ID do usuário autenticado que está a fazer o upload.
     * @return O objeto de domínio {@link Material} representando o ficheiro guardado.
     */
    Material executar(UploadMaterialCommand command, UUID usuarioId);
}
