package com.pdfocus.application.resumo.dto;

import java.util.UUID;

/**
 * Comando de entrada do caso de uso {@code CriarResumoUseCase}.
 * <p>
 * Contém os dados mínimos necessários para criar um novo resumo no sistema.
 * O identificador do usuário autenticado é fornecido pelo contexto de segurança,
 * e não pelo corpo da requisição.
 * </p>
 *
 * <p>
 * Este objeto segue o padrão <strong>Command DTO</strong> — imutável e simples —
 * projetado para trafegar entre as camadas de entrada (controller) e aplicação.
 * </p>
 *
 * @param disciplinaId Identificador da disciplina à qual o resumo pertence.
 * @param titulo        Título do resumo.
 * @param conteudo      Texto do conteúdo do resumo.
 *
 * @see com.pdfocus.application.resumo.service.DefaultCriarResumoService
 * @see com.pdfocus.application.resumo.port.entrada.CriarResumoUseCase
 */
public record CriarResumoCommand(
        UUID disciplinaId,
        String titulo,
        String conteudo
) {}
