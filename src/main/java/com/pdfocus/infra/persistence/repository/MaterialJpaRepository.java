package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.MaterialEntity;
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
}
