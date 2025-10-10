package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.ResumoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumoJpaRepository extends JpaRepository<ResumoEntity, UUID> {

    Optional<ResumoEntity> findByIdAndUsuarioId(UUID id ,UUID usuarioId);

    /**
     * Lista todos os resumos pertencentes a um usuário específico.
     * O Spring Data JPA criará a consulta automaticamente.
     *
     * @param usuarioId O ID do usuário.
     * @return Uma lista de {@link ResumoEntity} do usuário. Pode retornar uma lista vazia.
     */
    List<ResumoEntity> findAllByUsuarioId(UUID usuarioId);


    /**
     * Retorna todos os resumos associados a uma disciplina específica e a um usuário específico.
     * <p>
     * Este método utiliza a convenção de nomes do Spring Data JPA para gerar a consulta automaticamente.
     * Como o campo {@code disciplina} em {@code ResumoEntity} é um objeto do tipo {@code DisciplinaEntity},
     * usamos a navegação por atributo para acessar seu ID: {@code disciplina.id}.
     * Assim, o nome do método correto é {@code findAllByDisciplina_IdAndUsuarioId}.
     *
     * @param disciplinaId o ID da disciplina
     * @param usuarioId o ID do usuário
     * @return uma lista de objetos {@link com.pdfocus.infra.persistence.entity.ResumoEntity}, podendo estar vazia
     */
    List<ResumoEntity> findAllByDisciplina_IdAndUsuarioId(UUID disciplinaId, UUID usuarioId);


    /**
     * Deleta um resumo específico pelo seu ID e pelo ID do usuário proprietário.
     * O Spring Data JPA pode gerar a consulta de deleção.
     * O método pode retornar void ou long (número de registros deletados).
     *
     * @param id O ID do resumo a ser deletado.
     * @param usuarioId O ID do usuário proprietário do resumo.
     * @return O número de registros deletados (pode ser usado para verificar se a deleção ocorreu).
     * Se retornar void, a operação é "dispare e esqueça" do ponto de vista do retorno do método.
     */
    long deleteByIdAndUsuarioId(UUID id, UUID usuarioId);

    long countByUsuarioId(UUID usuarioId);

    /**
     * Encontra todos os resumos de uma disciplina específica (sem verificação de usuário).
     * Usado para operações de deleção em cascata onde já validamos a permissão do usuário.
     *
     * @param disciplinaId O ID da disciplina
     * @return Lista de resumos da disciplina
     */
    List<ResumoEntity> findByDisciplinaId(UUID disciplinaId);
}

