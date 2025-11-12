package com.pdfocus.application.material.port.entrada;

import com.pdfocus.application.material.dto.MaterialRecenteResponse;
import java.util.List;

/**
 * Porta de entrada (Input Port) do caso de uso responsável por buscar os materiais
 * mais recentes do usuário autenticado.
 *
 * <p>
 * Esta interface representa o contrato da aplicação para o fluxo de consulta dos
 * materiais adicionados mais recentemente. O objetivo é fornecer um resumo
 * rápido para o dashboard do usuário, mostrando seus últimos envios.
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b></p>
 * <ul>
 *   <li>Pertence à camada de <b>aplicação</b> (Application Layer).</li>
 *   <li>Implementada por {@code DefaultBuscarMateriaisRecentesService}.</li>
 *   <li>Consumida pela camada <b>adapters/web</b> (por exemplo, controladores REST).</li>
 *   <li>Exposta via DTOs ({@link MaterialRecenteResponse}) para garantir isolamento do domínio.</li>
 * </ul>
 */
public interface BuscarMateriaisRecentesUseCase {

    /**
     * Executa a lógica de negócio para recuperar os materiais mais recentes do
     * usuário atualmente autenticado.
     *
     * <p>
     * A implementação define internamente a quantidade de itens retornados (por exemplo,
     * os cinco mais recentes). Caso o usuário não possua materiais, a lista retornada
     * será vazia — nunca {@code null}.
     * </p>
     *
     * @return uma lista ordenada de {@link MaterialRecenteResponse}, do mais recente para o mais antigo.
     */
    List<MaterialRecenteResponse> executar();
}
