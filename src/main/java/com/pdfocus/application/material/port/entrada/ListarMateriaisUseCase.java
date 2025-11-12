package com.pdfocus.application.material.port.entrada;

import com.pdfocus.core.models.Material;
import java.util.List;
import java.util.UUID;

/**
 * Porta de entrada (Input Port) responsável pelo caso de uso de listagem
 * dos materiais associados a uma disciplina específica.
 *
 * <p>
 * Este caso de uso garante que apenas os materiais pertencentes ao utilizador
 * autenticado sejam retornados, preservando as regras de segurança e escopo de acesso.
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b></p>
 * <ul>
 *   <li>Pertence à camada de <b>aplicação</b> (Application Layer).</li>
 *   <li>Implementada por {@code DefaultListarMateriaisService} (ou equivalente).</li>
 *   <li>Consumida pela camada <b>adapters/web</b> (controladores REST ou GraphQL resolvers).</li>
 *   <li>Depende de portas de saída (Output Ports) como {@code MaterialRepository} e {@code UsuarioRepository}.</li>
 * </ul>
 *
 * <p>
 * A identidade do utilizador deve ser obtida pela implementação via contexto de
 * segurança (ex: {@code SecurityContextHolder} no Spring Security), não sendo
 * fornecida explicitamente no método.
 * </p>
 */
public interface ListarMateriaisUseCase {

    /**
     * Executa a lógica de negócio responsável por listar todos os materiais
     * pertencentes a uma disciplina específica do utilizador autenticado.
     *
     * <p>
     * A implementação deve validar a associação do utilizador e garantir
     * que a busca não exponha dados de outros perfis.
     * </p>
     *
     * @param disciplinaId o identificador único ({@link UUID}) da disciplina cujos materiais serão listados.
     * @return uma lista de objetos {@link Material} pertencentes à disciplina e ao utilizador autenticado.
     */
    List<Material> executar(UUID disciplinaId);
}
