package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de Repositório Spring Data JPA para a entidade {@link DisciplinaEntity}.
 * <p>
 * Estendendo {@link JpaRepository}, esta interface herda automaticamente
 * métodos CRUD (Create, Read, Update, Delete) básicos e funcionalidades de paginação e ordenação
 * para a entidade {@code DisciplinaEntity}.
 * <p>
 * Métodos de consulta personalizados podem ser adicionados aqui seguindo as convenções
 * de nomenclatura do Spring Data JPA ou utilizando a anotação {@code @Query}.
 */
@Repository
public interface DisciplinaJpaRepository  extends JpaRepository<DisciplinaEntity, UUID> {

    /**
     * Busca todas as disciplinas pertencentes a um usuário específico.
     * O Spring Data JPA criará a consulta automaticamente com base no nome do método.
     *
     * @param usuarioId O ID do usuário.
     * @return Uma lista de {@link DisciplinaEntity} do usuário.
     */
    List<DisciplinaEntity> findAllByUsuarioId(UUID usuarioId);

    /**
     * Busca uma disciplina específica pelo seu ID E pelo ID do usuário proprietário.
     * <p>
     * Garante que a consulta só retorne um resultado se ambos os IDs corresponderem,
     * implementando a segurança de dados (multi-tenancy) no nível do banco.
     *
     * @param id O ID da disciplina (PK).
     * @param usuarioId O ID do usuário (FK).
     * @return Um {@link Optional} contendo a {@link DisciplinaEntity} se encontrada, ou vazio.
     */
    Optional<DisciplinaEntity> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    /**
     * Conta o número total de disciplinas pertencentes a um usuário específico.
     *
     * @param usuarioId O ID do usuário.
     * @return O número total (long) de disciplinas do usuário.
     */
    long countByUsuarioId(UUID usuarioId);

}