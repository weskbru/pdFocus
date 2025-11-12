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
 * Controlador REST responsável pelos endpoints do dashboard do usuário.
 * <p>
 * Atua como ponto de integração entre o frontend e os casos de uso que agregam dados
 * de disciplinas, resumos e materiais — servindo como base para a tela principal do sistema.
 * </p>
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li><b>GET /dashboard/estatisticas</b> — Retorna os totais de disciplinas, resumos e materiais do usuário.</li>
 *   <li><b>GET /dashboard/materiais/recentes</b> — Retorna os últimos materiais enviados.</li>
 * </ul>
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final BuscarEstatisticasDashboardUseCase buscarEstatisticasDashboardUseCase;
    private final BuscarMateriaisRecentesUseCase buscarMateriaisRecentesUseCase;

    /**
     * Construtor com injeção dos casos de uso necessários para a construção do dashboard.
     *
     * @param buscarEstatisticasDashboardUseCase Caso de uso para buscar as estatísticas gerais.
     * @param buscarMateriaisRecentesUseCase Caso de uso para listar os materiais mais recentes.
     */
    public DashboardController(
            BuscarEstatisticasDashboardUseCase buscarEstatisticasDashboardUseCase,
            BuscarMateriaisRecentesUseCase buscarMateriaisRecentesUseCase
    ) {
        this.buscarEstatisticasDashboardUseCase = buscarEstatisticasDashboardUseCase;
        this.buscarMateriaisRecentesUseCase = buscarMateriaisRecentesUseCase;
    }

    /**
     * Retorna as estatísticas agregadas do dashboard do usuário autenticado.
     * <p>
     * O caso de uso {@link BuscarEstatisticasDashboardUseCase} encapsula toda a lógica
     * de agregação (quantidade de disciplinas, resumos e materiais).
     * </p>
     *
     * @return 200 (OK) com o DTO {@link DashboardEstatisticasResponse} no corpo da resposta.
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<DashboardEstatisticasResponse> buscarEstatisticas() {
        DashboardEstatisticasResponse response = buscarEstatisticasDashboardUseCase.executar();
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna uma lista dos materiais mais recentes do usuário autenticado.
     * <p>
     * Este endpoint serve para preencher a seção "Materiais Recentes" do dashboard.
     * </p>
     *
     * @return 200 (OK) com uma lista de {@link MaterialRecenteResponse}.
     */
    @GetMapping("/materiais/recentes")
    public ResponseEntity<List<MaterialRecenteResponse>> buscarMateriaisRecentes() {
        List<MaterialRecenteResponse> response = buscarMateriaisRecentesUseCase.executar();
        return ResponseEntity.ok(response);
    }
}
