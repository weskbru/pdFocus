package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository JPA responsável pelas operações de banco de dados da entidade Feedback.
 * * <p>Esta interface estende o {@link JpaRepository} do Spring Data, fornecendo métodos
 * padrão de CRUD e queries customizadas (JPQL) para atender às regras de negócio
 * e relatórios do PDFocus.</p>
 * * <p>Camada: Infraestrutura (Persistence)</p>
 */
@Repository
public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {

    /**
     * Verifica se existe algum feedback com a mesma mensagem (ignorando maiúsculas/minúsculas).
     * * <p>Utiliza uma query JPQL otimizada com {@code COUNT > 0} para evitar trazer
     * a entidade inteira do banco apenas para verificação.</p>
     *
     * @param mensagem A mensagem de texto a ser verificada.
     * @return {@code true} se já existe um feedback com o mesmo conteúdo; {@code false} caso contrário.
     */
    @Query("SELECT COUNT(f) > 0 FROM FeedbackEntity f WHERE LOWER(f.mensagem) = LOWER(:mensagem)")
    boolean existsByMensagemIgnoreCase(@Param("mensagem") String mensagem);

    /**
     * Busca uma lista de feedbacks filtrada pelo tipo (Sugestão, Bug, etc).
     *
     * @param tipo O tipo do feedback (ex: "BUG", "SUGGESTION").
     * @return Lista de entidades {@link FeedbackEntity} correspondentes.
     */
    List<FeedbackEntity> findByTipo(String tipo);

    /**
     * Busca uma lista de feedbacks filtrada pela nota de avaliação (Rating).
     *
     * @param rating A nota dada pelo usuário (ex: 1 a 5).
     * @return Lista de entidades {@link FeedbackEntity} com a nota especificada.
     */
    List<FeedbackEntity> findByRating(Integer rating);

    /**
     * Conta quantos feedbacks um usuário específico enviou dentro de um intervalo de tempo.
     * * <p><b>Uso principal:</b> Validar regras de negócio, como limitar a quantidade
     * de feedbacks diários por usuário (Anti-Spam).</p>
     * * <p>A query utiliza {@code >=} e {@code <=} para garantir que o intervalo seja inclusivo.</p>
     *
     * @param email O e-mail do usuário a ser verificado.
     * @param inicio A data/hora inicial do intervalo (ex: 00:00 do dia atual).
     * @param fim A data/hora final do intervalo (ex: 23:59 do dia atual).
     * @return O número total de feedbacks encontrados nesse período.
     */
    @Query("SELECT COUNT(f) FROM FeedbackEntity f " +
            "WHERE f.emailUsuario = :email " +
            "AND f.dataCriacao >= :inicio " +
            "AND f.dataCriacao <= :fim")
    long contarFeedbacksPorIntervalo(
            @Param("email") String email,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}