package com.pdfocus.application.material.port.saida;

import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de Saída (Output Port) responsável pela persistência e leitura de {@link Material}.
 *
 * <p>
 * Esta interface define o contrato entre a camada de Aplicação e a camada de Infraestrutura,
 * mantendo o domínio independente de frameworks ou tecnologias específicas (ex: JPA, JDBC, etc.).
 * </p>
 *
 * <p>
 * As implementações devem garantir:
 * <ul>
 *   <li>Integridade referencial entre Material, Disciplina e Usuário;</li>
 *   <li>Segurança no acesso aos registros (verificação de posse do usuário);</li>
 *   <li>Eficiência nas consultas, evitando N+1 quando aplicável.</li>
 * </ul>
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b> Parte da camada <i>Application → Infrastructure</i> do modelo Hexagonal.</p>
 */
public interface MaterialRepository {

    /**
     * Persiste um material no repositório.
     *
     * @param material O objeto de domínio a ser salvo.
     * @return O {@link Material} salvo (possivelmente com ID e timestamps preenchidos).
     */
    Material salvar(Material material);

    /**
     * Lista todos os materiais pertencentes a uma disciplina e a um usuário específico.
     *
     * @param disciplinaId O identificador da disciplina.
     * @param usuarioId O identificador do usuário proprietário.
     * @return Uma lista de {@link Material} filtrada por disciplina e usuário.
     */
    List<Material> listarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);

    /**
     * Busca um material pelo seu ID, garantindo que pertença ao usuário autenticado.
     *
     * @param id O identificador do material.
     * @param usuarioId O identificador do usuário proprietário.
     * @return Um {@link Optional} contendo o material, se encontrado e autorizado.
     */
    Optional<Material> buscarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Remove um material específico, garantindo a verificação de posse do usuário.
     *
     * @param id O identificador do material.
     * @param usuarioId O identificador do usuário proprietário.
     * @throws com.pdfocus.core.exceptions.material.MaterialNaoEncontradoException
     *         se o material não for encontrado ou não pertencer ao usuário.
     */
    void deletarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Conta o número total de materiais associados a um usuário.
     *
     * @param usuario O proprietário dos materiais.
     * @return O total de materiais encontrados.
     */
    long countByUsuario(Usuario usuario);

    /**
     * Retorna os 5 materiais mais recentes de um usuário (consulta simples, sem JOIN).
     *
     * <p><b>Nota:</b> Pode causar N+1 se as disciplinas forem acessadas fora do escopo de fetch controlado.</p>
     *
     * @param usuario O usuário autenticado.
     * @return Uma lista contendo até 5 {@link Material} mais recentes.
     */
    List<Material> buscar5MaisRecentesPorUsuario(Usuario usuario);

    /**
     * Busca um material pelo seu identificador, sem verificação de posse.
     * <p>Usado em cenários internos onde a segurança já é garantida em outro nível.</p>
     *
     * @param id O identificador do material.
     * @return Um {@link Optional} contendo o material, se encontrado.
     */
    Optional<Material> buscarPorId(UUID id);

    /**
     * Retorna os materiais de uma disciplina de forma paginada.
     *
     * @param disciplinaId O identificador da disciplina.
     * @param pageable As informações de paginação.
     * @return Uma página de {@link Material}.
     */
    Page<Material> buscarPorDisciplinaDeFormaPaginada(UUID disciplinaId, Pageable pageable);

    /**
     * Exclui todos os materiais associados a uma disciplina específica.
     *
     * @param disciplinaId O identificador da disciplina.
     */
    void deletarTodosPorDisciplinaId(UUID disciplinaId);

    /**
     * Variante otimizada do método {@link #buscar5MaisRecentesPorUsuario(Usuario)},
     * com <b>JOIN FETCH</b> da entidade {@link com.pdfocus.core.models.Disciplina}.
     *
     * <p>
     * Evita o problema clássico de N+1 queries ao carregar as disciplinas associadas
     * em uma única consulta.
     * </p>
     *
     * @param usuario O usuário autenticado.
     * @return Uma lista de {@link Material} com suas disciplinas pré-carregadas.
     */
    List<Material> buscar5MaisRecentesPorUsuarioComDisciplina(Usuario usuario);
}
