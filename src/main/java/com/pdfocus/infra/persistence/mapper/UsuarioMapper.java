package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitária responsável por mapear (converter) objetos entre
 * a entidade de domínio {@link Usuario} e a entidade JPA {@link UsuarioEntity}.
 */
@Component
public class UsuarioMapper {

    /**
     * Converte um objeto de domínio {@link Usuario} para uma entidade JPA {@link UsuarioEntity}.
     *
     * @param usuario O objeto de domínio Usuario a ser convertido.
     * @return A {@link UsuarioEntity} correspondente, pronta para persistência.
     */
    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        // CORREÇÃO: Adicionados os campos de feedback no construtor da Entity
        // Certifique-se de que sua UsuarioEntity possui um construtor com essa assinatura
        // ou altere para usar setters se preferir (ex: entity.setFeedbacksHoje(...)).
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenhaHash(),
                usuario.isAtivo(),
                usuario.getResumosHoje(),
                usuario.getDataUltimoUso(),
                usuario.getFeedbacksHoje(),      // <--- Novo Campo
                usuario.getDataUltimoFeedback()  // <--- Novo Campo
        );
    }

    /**
     * Converte uma entidade JPA {@link UsuarioEntity} para um objeto de domínio {@link Usuario}.
     *
     * @param entity A entidade JPA UsuarioEntity a ser convertida.
     * @return O objeto de domínio {@link Usuario} correspondente.
     */
    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;

        // Mapeia todos os campos, incluindo os contadores de Resumo e Feedback
        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getSenhaHash(), // Geralmente na Entity o campo chama 'senha', mas contém o Hash
                entity.isAtivo(),
                entity.getResumosHoje(),
                entity.getDataUltimoUso(),
                entity.getFeedbacksHoje(),       // <--- Mapeamento do novo campo
                entity.getDataUltimoFeedback()   // <--- Mapeamento do novo campo
        );
    }

    /**
     * Converte uma lista de entidades JPA {@link UsuarioEntity} para uma lista de objetos de domínio {@link Usuario}.
     *
     * @param entities A lista de {@code UsuarioEntity} a ser convertida.
     * @return Uma lista de {@link Usuario}. Retorna uma lista vazia se a entrada for nula ou vazia.
     */
    public List<Usuario> toDomainList(List<UsuarioEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}