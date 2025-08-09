package com.pdfocus.application.material.port.entrada;

import java.util.UUID;

/**
 * Caso de uso para deletar um material de estudo existente.
 */
public interface DeletarMaterialUseCase {

    /**
     * Executa a deleção de um material, garantindo que ele pertença ao usuário.
     * A operação deve remover tanto o registro no banco de dados quanto o arquivo físico.
     *
     * @param id O ID do material a ser deletado.
     * @param usuarioId O ID do usuário que deve ser o proprietário do material.
     */
    void executar(UUID id, UUID usuarioId);
}
