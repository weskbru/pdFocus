package com.pdfocus.application.material.port.entrada;

import com.pdfocus.core.models.Material;

import java.util.List;
import java.util.UUID;

/**
 * Caso de uso para listar os materiais de estudo de uma disciplina específica
 * pertencentes a um usuário.
 */
public interface ListarMateriaisUseCase {

    /**
     * Executa a busca de todos os materiais de uma disciplina para o usuário autenticado.
     *
     * @param disciplinaId O ID da disciplina cujos materiais serão listados.
     * @param usuarioId O ID do usuário autenticado.
     * @return Uma lista de {@link Material} contendo os metadados dos arquivos.
     */
    List<Material> executar(UUID disciplinaId, UUID usuarioId);
}
