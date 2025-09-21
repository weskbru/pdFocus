package com.pdfocus.application.material.port.entrada;

import java.util.UUID;

/**
 * Define o contrato (Porta de Entrada) para o caso de uso de apagar
 * um material existente, garantindo que ele pertença ao
 * utilizador autenticado.
 */
public interface DeletarMaterialUseCase {

    /**
     * Executa a lógica de negócio para apagar um material específico.
     * A segurança (garantir que o material pertence ao utilizador logado) é uma
     * responsabilidade da implementação.
     *
     * @param id O UUID do material a ser apagado.
     */
    void executar(UUID id);
}
