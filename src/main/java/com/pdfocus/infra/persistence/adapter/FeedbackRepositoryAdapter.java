package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.feedback.port.saida.FeedbackRepository;
import com.pdfocus.core.models.Feedback;
import com.pdfocus.infra.persistence.entity.FeedbackEntity;
import com.pdfocus.infra.persistence.mapper.FeedbackMapper;
import com.pdfocus.infra.persistence.repository.FeedbackJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter para o repositório de Feedback.
 * Implementa o port de saída conectando a Application Layer com a Infrastructure Layer.
 * Segue o mesmo padrão dos outros adapters (DisciplinaRepositoryAdapter, etc).
 */
@Component
public class FeedbackRepositoryAdapter implements FeedbackRepository {

    private final FeedbackJpaRepository feedbackJpaRepository;

    /**
     * Construtor com injeção de dependência seguindo o padrão do projeto.
     */
    public FeedbackRepositoryAdapter(FeedbackJpaRepository feedbackJpaRepository) {
        this.feedbackJpaRepository = feedbackJpaRepository;
    }

    /**
     * Salva um feedback no repositório.
     * Segue o padrão dos outros adapters com conversão Entity ↔ Domain.
     *
     * @param feedback Entidade de domínio a ser persistida
     * @return Feedback salvo com ID gerado
     */
    @Override
    public Feedback salvar(Feedback feedback) {
        // Converte domínio para entity
        FeedbackEntity entity = FeedbackMapper.toEntity(feedback);

        // Salva no repositório JPA
        FeedbackEntity entitySalva = feedbackJpaRepository.save(entity);

        // Converte de volta para domínio
        return FeedbackMapper.toDomain(entitySalva);
    }

    /**
     * Busca um feedback pelo ID.
     * Segue o padrão Optional para evitar null pointers.
     *
     * @param id ID do feedback
     * @return Optional contendo o feedback se encontrado
     */
    @Override
    public Optional<Feedback> buscarPorId(Long id) {
        Optional<FeedbackEntity> entityOptional = feedbackJpaRepository.findById(id);

        return entityOptional.map(FeedbackMapper::toDomain);
    }

    /**
     * Verifica se existe algum feedback com a mesma mensagem (para evitar duplicatas).
     * Segue o padrão de métodos de verificação dos outros repositories.
     *
     * @param mensagem Mensagem do feedback
     * @return true se já existe feedback com mesma mensagem
     */
    @Override
    public boolean existeComMensagem(String mensagem) {
        return feedbackJpaRepository.existsByMensagemIgnoreCase(mensagem);
    }

    @Override
    public List<Feedback> findAll(Sort dataCriacao) {
        return List.of();
    }
}