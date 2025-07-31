package com.pdfocus.application.resumo.dto;

import java.util.UUID;

/**
 * Comando para carregar os dados necessários para criar um novo resumo.
 * O ID do usuário será obtido do contexto de autenticação, não do corpo da requisição.
 *
 * @param disciplinaId O ID da disciplina à qual o resumo está associado.
 * @param titulo       O título do resumo.
 * @param conteudo     O conteúdo textual do resumo.
 */
public record CriarResumoCommand(
        UUID disciplinaId,
        String titulo,
        String conteudo
) {}