package com.pdfocus.application.material.port.entrada;

import com.pdfocus.core.models.Material;
import java.util.List;
import java.util.UUID;

/**
 * Define o contrato (Porta de Entrada) para o caso de uso de listar
 * todos os materiais de uma disciplina específica, garantindo que a busca
 * seja restrita ao utilizador autenticado.
 */
public interface ListarMateriaisUseCase {

    /**
     * Executa a lógica de negócio para buscar todos os materiais de uma disciplina.
     * A identidade do utilizador é obtida implicitamente a partir do contexto de
     * segurança.
     *
     * @param disciplinaId O ID da disciplina cujos materiais serão listados.
     * @return Uma lista de {@link Material} pertencentes à disciplina e ao utilizador.
     */
    List<Material> executar(UUID disciplinaId);
}
