package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitária responsável por mapear (converter) objetos entre
 * a entidade de domínio {@link Usuario} e a entidade JPA {@link UsuarioEntity}.
 */
public class UsuarioMapper {

    /**
     * Construtor privado para impedir a instanciação da classe utilitária.
     */
    private UsuarioMapper() {
        // Impede a instanciação
    }

    /**
     * Converte um objeto de domínio {@link Usuario} para uma entidade JPA {@link UsuarioEntity}.
     *
     * @param usuario O objeto de domínio Usuario a ser convertido.
     * @return A {@link UsuarioEntity} correspondente, pronta para persistência.
     */
    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        // Usa o construtor da UsuarioEntity (provavelmente gerado pelo Lombok)
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenhaHash()
        );
    }

    /**
     * Converte uma entidade JPA {@link UsuarioEntity} para um objeto de domínio {@link Usuario}.
     *
     * @param usuarioEntity A entidade JPA UsuarioEntity a ser convertida.
     * @return O objeto de domínio {@link Usuario} correspondente.
     */
    public static Usuario toDomain(UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) {
            return null;
        }

        // Usa o construtor publico do modelo de domínio Usuario
        return new Usuario(
                usuarioEntity.getId(),
                usuarioEntity.getNome(),
                usuarioEntity.getEmail(),
                usuarioEntity.getSenhaHash()
        );
    }

    /**
     * Converte uma lista de entidades JPA {@link UsuarioEntity} para uma lista de objetos de domínio {@link Usuario}.
     *
     * @param entities A lista de {@code UsuarioEntity} a ser convertida.
     * @return Uma lista de {@link Usuario}. Retorna uma lista vazia se a entrada for nula ou vazia.
     */
    public static List<Usuario> toDomainList(List<UsuarioEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }
}