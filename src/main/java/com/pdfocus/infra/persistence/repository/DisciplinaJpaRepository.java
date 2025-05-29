package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}
