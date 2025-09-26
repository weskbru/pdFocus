package com.pdfocus.application.material.dto;

import java.util.UUID;

/**
 * Comando para solicitar a deleção de um material.
 *
 * @param materialId O ID do material a ser deletado.
 * @param usuarioId  O ID do usuário que é dono do material.
 */
public record DeletarMaterialCommand(UUID materialId, UUID usuarioId) {
}