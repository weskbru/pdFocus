package com.pdfocus.application.dashboard.dto;

/**
 * DTO (Data Transfer Object) que encapsula as estatísticas do dashboard do usuário.
 *
 * <p>Utilizado para transferir informações agregadas para a interface de usuário,
 * como número total de disciplinas, resumos e materiais carregados.</p>
 *
 * @param totalDisciplinas O número total de disciplinas cadastradas pelo usuário.
 * @param resumosCriados   O número total de resumos criados pelo usuário.
 * @param totalMateriais   O número total de materiais (PDFs, vídeos, etc.) que o usuário subiu.
 */
public record DashboardEstatisticasResponse(
        long totalDisciplinas,
        long resumosCriados,
        long totalMateriais
) {
}
