package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.MaterialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de Repositório Spring Data JPA para a entidade {@link MaterialEntity}.
 * <p>
 * Fornece métodos CRUD e consultas personalizadas (derivadas do nome do método ou @Query)
 * para interagir com a tabela de materiais no banco de dados.
 * </p>
 */
@Repository
public interface MaterialJpaRepository extends JpaRepository<MaterialEntity, UUID> {

    /**
     * Busca todos os materiais por ID da disciplina e ID do usuário.
     * Garante que o usuário só possa ver materiais de disciplinas que lhe pertencem.
     * O Spring Data JPA cria a consulta navegando pelo relacionamento:
     * 'disciplina' (campo na MaterialEntity) -> 'id' (campo na DisciplinaEntity).
     *
     * @param disciplinaId O ID da disciplina.
     * @param usuarioId O ID do usuário proprietário.
     * @return Lista de materiais filtrados.
     */
    List<MaterialEntity> findAllByDisciplina_IdAndUsuarioId(UUID disciplinaId, UUID usuarioId);

    /**
     * Busca um material específico pelo seu ID e pelo ID do usuário proprietário.
     * Essencial para segurança (multi-tenancy) em operações de GET, UPDATE ou DELETE.
     *
     * @param id O ID do material (PK).
     * @param usuarioId O ID do usuário proprietário.
     * @return Um Optional contendo o MaterialEntity, se encontrado e pertencente ao usuário.
     */
    Optional<MaterialEntity> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    /**
     * Conta o número total de materiais pertencentes a um usuário.
     *
     * @param usuarioId O ID do usuário.
     * @return O número (long) de materiais.
     */
    long countByUsuarioId(UUID usuarioId);

    /**
     * Busca os 5 materiais mais recentes de um usuário (sem JOIN).
     * <p>
     * - "findFirst5": Limita o resultado aos 5 primeiros registros.
     * - "ByUsuarioId": Filtra pelo ID do usuário.
     * - "OrderByDataUploadDesc": Ordena pela 'dataUpload' decrescente.
     * <p>
     * <b>Nota:</b> Esta consulta causa o problema N+1 se a disciplina
     * for acessada em um loop no service.
     *
     * @param usuarioId O ID do usuário.
     * @return Lista dos 5 materiais mais recentes.
     */
    List<MaterialEntity> findFirst5ByUsuarioIdOrderByDataUploadDesc(UUID usuarioId);

    // --- A MELHORIA (N+1 FIX) ---

    /**
     * <b>[MELHORIA - N+1 FIX]</b>
     * Busca os materiais de um usuário com suas disciplinas associadas (JOIN FETCH).
     * <p>
     * Esta é a consulta otimizada para o dashboard. O {@code JOIN FETCH} força
     * o JPA a trazer a entidade {@code disciplina} associada na mesma query,
     * evitando o problema N+1 no service layer.
     * </p>
     * <p>
     * A limitação (ex: Top 5) será feita através do parâmetro {@link Pageable}
     * passado pelo Adaptador.
     * </p>
     *
     * @param usuarioId O ID do usuário.
     * @param pageable A configuração de paginação (ex: PageRequest.of(0, 5) para o Top 5).
     * @return Uma lista (página) de MaterialEntity com as DisciplinaEntity pré-carregadas.
     */
    @Query("SELECT m FROM MaterialEntity m JOIN FETCH m.disciplina " +
            "WHERE m.usuarioId = :usuarioId " +
            "ORDER BY m.dataUpload DESC")
    List<MaterialEntity> findRecentWithDisciplina(@Param("usuarioId") UUID usuarioId, Pageable pageable);

    // --- FIM DA MELHORIA ---

    /**
     * Busca materiais de uma disciplina de forma paginada.
     * O Spring Data JPA lê o nome deste método e automaticamente cria a consulta.
     *
     * @param disciplinaId O ID da disciplina para filtrar.
     * @param pageable As informações de paginação.
     * @return Uma página de entidades de material.
     */
    Page<MaterialEntity> findByDisciplinaId(UUID disciplinaId, Pageable pageable);

    /**
     * Encontra todos os materiais de uma disciplina específica (sem verificação de usuário).
     * Usado para operações internas, como deleção em cascata, onde a permissão
     * do usuário já foi validada em um nível superior (no service).
     *
     * @param disciplinaId O ID da disciplina
     * @return Lista de materiais da disciplina
     */
    List<MaterialEntity> findByDisciplinaId(UUID disciplinaId);
}