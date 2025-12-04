package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Feedback;
import com.pdfocus.infra.persistence.entity.FeedbackEntity;

/**
 * Mapper para conversão entre a entidade de domínio Feedback e a entidade JPA FeedbackEntity.
 * Segue o mesmo padrão dos outros mappers (DisciplinaMapper, MaterialMapper, etc).
 */
public class FeedbackMapper {

    public FeedbackMapper(UsuarioMapper usuarioMapper) {
    }

    /**
     * Converte uma entidade de domínio Feedback para FeedbackEntity (JPA).
     * Segue o padrão dos outros mappers com método estático.
     *
     * @param feedback Entidade de domínio
     * @return FeedbackEntity para persistência
     */
    public static FeedbackEntity toEntity(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(feedback.getId());
        entity.setTipo(feedback.getTipo());
        entity.setRating(feedback.getRating());
        entity.setMensagem(feedback.getMensagem());
        entity.setEmailUsuario(feedback.getEmailUsuario());
        entity.setPagina(feedback.getPagina());
        entity.setUserAgent(feedback.getUserAgent());
        entity.setDataCriacao(feedback.getDataCriacao());

        return entity;
    }

    /**
     * Converte uma FeedbackEntity (JPA) para entidade de domínio Feedback.
     * Segue o padrão dos outros mappers com método estático.
     *
     * @param entity Entidade JPA
     * @return Feedback de domínio
     */
    public static Feedback toDomain(FeedbackEntity entity) {
        if (entity == null) {
            return null;
        }

        // Usa o construtor de reconstituição para entidade do banco
        return new Feedback(
                entity.getId(),
                entity.getTipo(),
                entity.getRating(),
                entity.getMensagem(),
                entity.getEmailUsuario(),
                entity.getPagina(),
                entity.getUserAgent(),
                entity.getDataCriacao()
        );
    }
}