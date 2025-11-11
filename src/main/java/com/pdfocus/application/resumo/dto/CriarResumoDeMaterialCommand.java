package com.pdfocus.application.resumo.dto;

import java.util.UUID;

/**
 * Comando (DTO) usado para criar um resumo a partir de um material PDF existente.
 * <p>
 * Este comando transporta os dados necessários do frontend (ou de outro serviço)
 * até o caso de uso {@code GerarResumoAutomaticoUseCase}.
 * O {@code usuarioId} é obtido automaticamente do contexto de autenticação.
 * </p>
 *
 * <p>Os campos são usados para orquestrar o processo de:</p>
 * <ul>
 *   <li>Identificar o material que servirá de base para a extração de texto;</li>
 *   <li>Associar o resumo à disciplina correta;</li>
 *   <li>Definir título e conteúdo, seja gerado automaticamente, seja editado pelo usuário.</li>
 * </ul>
 *
 * @param materialId  ID do material (PDF) usado como base para o resumo.
 * @param disciplinaId ID da disciplina associada.
 * @param titulo       Título do resumo — pode ser definido pelo usuário ou gerado automaticamente.
 * @param conteudo     Texto do resumo — pode ser parcialmente extraído do PDF ou ajustado manualmente.
 */
public record CriarResumoDeMaterialCommand(
        UUID materialId,
        UUID disciplinaId,
        String titulo,
        String conteudo
) {}
