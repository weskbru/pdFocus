package com.pdfocus.application.material.port.entrada;


import com.pdfocus.application.material.dto.MaterialRecenteResponse;
import java.util.List;

/**
 * Define o contrato (a "Porta de Entrada") para o caso de uso de buscar
 * uma lista dos materiais mais recentes adicionados pelo usuário logado.
 */
public interface BuscarMateriaisRecentesUseCase {

    /**
     * Executa a lógica para buscar os materiais mais recentes do usuário autenticado.
     *
     * A implementação definirá a quantidade de materiais a serem retornados (ex: os últimos 5).
     *
     * @return uma Lista de {@link MaterialRecenteResponse}, ordenada do mais recente
     * para o mais antigo. A lista pode ser vazia se o usuário não tiver materiais.
     */
    List<MaterialRecenteResponse> executar();
}