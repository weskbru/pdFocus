package com.pdfocus.application.resumo.dto;

import java.util.UUID;

/**
 * Comando para criar um resumo baseado em um material (PDF) existente.
 * O ID do usuário será obtido do contexto de autenticação.
 *
 * @param materialId O ID do material (PDF) que será usado como base para o resumo
 * @param disciplinaId O ID da disciplina à qual o resumo está associado
 * @param titulo O título do resumo (pode ser gerado automaticamente ou fornecido pelo usuário)
 * @param conteudo O conteúdo textual do resumo (pode ser pré-preenchido com texto extraído do PDF)
 */
public record CriarResumoDeMaterialCommand(
        UUID materialId,
        UUID disciplinaId,
        String titulo,
        String conteudo
) {}