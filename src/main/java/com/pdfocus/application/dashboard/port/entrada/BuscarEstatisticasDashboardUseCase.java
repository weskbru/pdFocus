package com.pdfocus.application.dashboard.port.entrada;

import com.pdfocus.application.dashboard.dto.DashboardEstatisticasResponse;

/**
 * Porta de entrada (Use Case) responsável por fornecer as estatísticas principais
 * para o dashboard do usuário autenticado.
 *
 * <p>Implementações desta interface devem coletar, agregar e retornar os dados
 * de forma que o dashboard possa exibir informações resumidas e atualizadas,
 * como total de disciplinas, resumos criados e materiais enviados.</p>
 */
public interface BuscarEstatisticasDashboardUseCase {

    /**
     * Executa o caso de uso de busca das estatísticas do dashboard.
     *
     * @return um {@link DashboardEstatisticasResponse} contendo os dados consolidados
     *         de disciplinas, resumos e materiais do usuário.
     */
    DashboardEstatisticasResponse executar();
}
