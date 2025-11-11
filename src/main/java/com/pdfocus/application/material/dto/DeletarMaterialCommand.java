package com.pdfocus.application.material.dto;

import java.util.UUID;

/**
 * Comando que encapsula os dados necessários para a remoção de um material
 * pertencente a um usuário autenticado.
 *
 * <p>
 * Este objeto é utilizado como <b>mensagem de entrada</b> (Command Pattern)
 * para o caso de uso de deleção de materiais. Ele expressa a intenção da ação
 * sem expor detalhes de implementação.
 * </p>
 *
 * <p><b>Garantias de integridade:</b></p>
 * <ul>
 *   <li>O {@code materialId} deve referenciar um material existente.</li>
 *   <li>O {@code usuarioId} deve corresponder ao proprietário do material.</li>
 * </ul>
 *
 * @param materialId Identificador único do material a ser deletado.
 * @param usuarioId Identificador único do usuário dono do material.
 */
public record DeletarMaterialCommand(UUID materialId, UUID usuarioId) {
}
