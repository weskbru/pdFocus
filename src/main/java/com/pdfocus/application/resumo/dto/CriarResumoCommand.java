package com.pdfocus.application.resumo.dto;

import java.util.UUID;

/**
 * Comando para carregar os dados necessários para criar um novo resumo.
 *
 * @param usuarioId O ID do usuário proprietário do resumo.
 * @param disciplinaId O ID da disciplina à qual o resumo está associado.
 * @param titulo O título do resumo.
 * @param conteudo O conteúdo textual do resumo.
 */
public record CriarResumoCommand(
        UUID usuarioId,
        UUID disciplinaId, // Nome do campo agora corresponde exatamente ao JSON
        String titulo,
        String conteudo
) {}