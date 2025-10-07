package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.MaterialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de Repositório Spring Data JPA para a entidade {@link MaterialEntity}.
 */
@Repository
public interface MaterialJpaRepository extends JpaRepository<MaterialEntity, UUID> {

    /**
     * Busca todos os materiais por ID da disciplina e ID do usuário.
     * O Spring Data JPA cria a consulta navegando pelo relacionamento:
     * 'disciplina' (campo na MaterialEntity) -> 'id' (campo na DisciplinaEntity).
     */
    List<MaterialEntity> findAllByDisciplina_IdAndUsuarioId(UUID disciplinaId, UUID usuarioId);

    /**
     * Busca um material pelo seu ID e pelo ID do usuário proprietário.
     */
    Optional<MaterialEntity> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    long countByUsuarioId(UUID usuarioId);

    /**
     * - "findFirst5": Limita o resultado aos 5 primeiros registros.
     * - "ByUsuarioId": Filtra os materiais pelo ID do usuário.
     * - "OrderByDataUploadDesc": Ordena os resultados pela coluna 'dataUpload'
     * em ordem decrescente (do mais novo para o mais antigo).
     *
     * O Spring Data JPA lê este nome e automaticamente cria a query SQL complexa para nós!
     */
    List<MaterialEntity> findFirst5ByUsuarioIdOrderByDataUploadDesc(UUID usuarioId);

    /**
     * O Spring Data JPA lê o nome deste método e automaticamente cria a consulta:
     * "SELECT * FROM materiais WHERE disciplina_id = ? LIMIT ? OFFSET ?"
     *
     * @param disciplinaId O ID da disciplina para filtrar.
     * @param pageable As informações de paginação.
     * @return Uma página de entidades de material.
     */
    Page<MaterialEntity> findByDisciplinaId(UUID disciplinaId, Pageable pageable);
}
