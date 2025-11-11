package com.pdfocus.application.material.port.entrada;

import java.util.UUID;

/**
 * Porta de entrada (Input Port) do caso de uso responsável por apagar um material existente.
 *
 * <p>
 * Este caso de uso executa a operação de exclusão lógica ou física (dependendo da
 * política definida pela aplicação) de um material pertencente ao usuário autenticado.
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b></p>
 * <ul>
 *   <li>Pertence à camada de <b>aplicação</b> (Application Layer).</li>
 *   <li>Implementada por {@code DefaultDeletarMaterialService} (ou equivalente).</li>
 *   <li>Consumida pela camada <b>adapters/web</b>, geralmente por controladores REST.</li>
 *   <li>Comunica-se com a camada de persistência via portas de saída (Output Ports).</li>
 * </ul>
 *
 * <p>
 * A verificação de autorização (se o material pertence ao usuário logado)
 * deve ser garantida pela implementação concreta deste caso de uso.
 * </p>
 */
public interface DeletarMaterialUseCase {

    /**
     * Executa a lógica de negócio para remover um material específico do sistema.
     *
     * <p>
     * Caso o material não pertença ao usuário autenticado, a implementação deve lançar
     * uma exceção apropriada (por exemplo, {@code AccessDeniedException} ou similar).
     * </p>
     *
     * @param id o identificador único ({@link UUID}) do material a ser removido.
     */
    void executar(UUID id);
}
