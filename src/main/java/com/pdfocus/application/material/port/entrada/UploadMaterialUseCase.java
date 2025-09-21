package com.pdfocus.application.material.port.entrada;

import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.core.models.Material;

/**
 * Define o contrato (Porta de Entrada) para o caso de uso de fazer o upload
 * de um novo material para o utilizador autenticado.
 */
public interface UploadMaterialUseCase {

    /**
     * Executa a lógica de negócio para o upload de um novo material.
     * A identidade do utilizador é obtida implicitamente a partir do contexto de
     * segurança, garantindo que o material seja associado ao proprietário correto.
     *
     * @param command O DTO contendo os dados do ficheiro e a qual disciplina pertence.
     * @return O objeto de domínio {@link Material} recém-criado.
     */
    Material executar(UploadMaterialCommand command);
}
