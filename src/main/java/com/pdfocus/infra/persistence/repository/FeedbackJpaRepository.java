package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA para operações de banco de dados com Feedback.
 * Segue o mesmo padrão dos outros JPA repositories (DisciplinaJpaRepository, etc).
 */
@Repository
public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {

    /**
     * Verifica se existe algum feedback com a mesma mensagem (case-insensitive).
     * Usando @Query nativa do PostgreSQL para case-insensitive.
     *
     * @param mensagem Mensagem do feedback
     * @return true se já existe feedback com mesma mensagem
     */
    @Query("SELECT COUNT(f) > 0 FROM FeedbackEntity f WHERE LOWER(f.mensagem) = LOWER(:mensagem)")
    boolean existsByMensagemIgnoreCase(@Param("mensagem") String mensagem);

    /**
     * Busca feedbacks por tipo.
     * Segue o padrão de métodos de query do Spring Data JPA.
     *
     * @param tipo Tipo do feedback
     * @return Lista de feedbacks do tipo especificado
     */
    List<FeedbackEntity> findByTipo(String tipo);

    /**
     * Busca feedbacks por rating.
     *
     * @param rating Rating do feedback
     * @return Lista de feedbacks com o rating especificado
     */
    List<FeedbackEntity> findByRating(Integer rating);
}