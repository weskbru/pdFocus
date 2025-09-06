package com.pdfocus.application.dashboard.dto;

/**
 * DTO (Data Transfer Object) que encapsula os dados de estatísticas
 * para serem exibidos no dashboard do usuário.
 *
 * @param totalDisciplinas O número total de disciplinas cadastradas pelo usuário.
 * @param resumosCriados   O número total de resumos criados pelo usuário.
 * @param totalMateriais   O número total de materiais (PDFs, etc.) que o usuário subiu.
 */
public record DashboardEstatisticasResponse(
        long totalDisciplinas,
        long resumosCriados,
        long totalMateriais
) {
}