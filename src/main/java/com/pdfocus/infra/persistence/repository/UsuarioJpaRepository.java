package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.ResumoEntity;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de Repositório Spring Data JPA para a entidade {@link UsuarioEntity}.
 * <p>
 * Herda métodos CRUD básicos de {@link JpaRepository} e define
 * consultas personalizadas para a entidade Usuário.
 * </p>
 */
@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, UUID> {

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     * <p>
     * O Spring Data JPA criará a implementação deste método automaticamente
     * com base no seu nome, gerando uma consulta para encontrar um {@link UsuarioEntity}
     * onde o campo 'email' corresponda ao parâmetro fornecido.
     * </p>
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return Um {@link Optional} contendo a {@link UsuarioEntity} se encontrada,
     * ou um Optional vazio caso contrário.
     */
    Optional<UsuarioEntity> findByEmail(String email);

}