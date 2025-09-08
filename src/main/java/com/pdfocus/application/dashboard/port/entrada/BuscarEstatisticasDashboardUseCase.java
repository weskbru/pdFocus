package com.pdfocus.application.dashboard.port.entrada;

import com.pdfocus.application.dashboard.dto.DashboardEstatisticasResponse;

/**
 * Define o contrato (a "Porta de Entrada") para o caso de uso de buscar
 * as estatísticas principais para o dashboard do usuário logado.
 */
public interface BuscarEstatisticasDashboardUseCase {

    /**
     * Executa a lógica de negócio para coletar e agregar as estatísticas
     * relevantes para o usuário autenticado.
     *
     * @return um DTO {@link DashboardEstatisticasResponse} contendo os dados consolidados.
     */
    DashboardEstatisticasResponse executar();
}