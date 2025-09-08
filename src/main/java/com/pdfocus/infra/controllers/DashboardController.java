package com.pdfocus.infra.controllers;

import com.pdfocus.application.dashboard.dto.DashboardEstatisticasResponse;
import com.pdfocus.application.dashboard.port.entrada.BuscarEstatisticasDashboardUseCase;
import com.pdfocus.application.material.dto.MaterialRecenteResponse;
import com.pdfocus.application.material.port.entrada.BuscarMateriaisRecentesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para agrupar os endpoints relacionados ao dashboard.
 * Expõe funcionalidades de visualização de dados agregados para o frontend.
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final BuscarEstatisticasDashboardUseCase buscarEstatisticasDashboardUseCase;
    private final BuscarMateriaisRecentesUseCase buscarMateriaisRecentesUseCase;

    public DashboardController(BuscarEstatisticasDashboardUseCase buscarEstatisticasDashboardUseCase,
                               BuscarMateriaisRecentesUseCase buscarMateriaisRecentesUseCase) {
        this.buscarEstatisticasDashboardUseCase = buscarEstatisticasDashboardUseCase;
        this.buscarMateriaisRecentesUseCase = buscarMateriaisRecentesUseCase;
    }

    /**
     * Endpoint para buscar as estatísticas agregadas do usuário logado (total de
     * disciplinas, resumos e materiais).
     *
     * @return ResponseEntity com status 200 (OK) e o DTO {@link DashboardEstatisticasResponse} no corpo.
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<DashboardEstatisticasResponse> buscarEstatisticas() {
        // Delega a execução da lógica de negócio para a camada de aplicação (Caso de Uso).
        DashboardEstatisticasResponse response = buscarEstatisticasDashboardUseCase.executar();

        // Retorna a resposta encapsulada em um ResponseEntity.
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para buscar uma lista dos materiais mais recentes do usuário logado.
     *
     * @return ResponseEntity com status 200 (OK) e uma lista de {@link MaterialRecenteResponse} no corpo.
     */
    @GetMapping("/materiais/recentes")
    public ResponseEntity<List<MaterialRecenteResponse>> buscarMateriaisRecentes() {
        // Delega a execução para o caso de uso específico.
        List<MaterialRecenteResponse> response = buscarMateriaisRecentesUseCase.executar();

        // Retorna a lista na resposta.
        return ResponseEntity.ok(response);
    }
}