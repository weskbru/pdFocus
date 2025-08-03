package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Material;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.MaterialEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe utilitária responsável por mapear (converter) objetos entre
 * a entidade de domínio {@link Material} e a entidade JPA {@link MaterialEntity}.
 * Utiliza o {@link DisciplinaMapper} para converter a disciplina associada.
 */
public class MaterialMapper {

    /**
     * Construtor privado para impedir a instanciação da classe utilitária.
     */
    private MaterialMapper() {
        // Impede a instanciação
    }

    /**
     * Converte um objeto de domínio {@link Material} para uma entidade JPA {@link MaterialEntity}.
     *
     * @param material O objeto de domínio Material a ser convertido.
     * @return A {@link MaterialEntity} correspondente, ou {@code null} se a entrada for {@code null}.
     */
    public static MaterialEntity toEntity(Material material) {
        if (material == null) {
            return null;
        }

        throw new UnsupportedOperationException("Use o método toEntity que recebe a DisciplinaEntity.");
    }

    /**
     * Converte um objeto de domínio {@link Material} para uma entidade JPA {@link MaterialEntity},
     * utilizando uma {@link DisciplinaEntity} já buscada.
     *
     * @param material O objeto de domínio Material a ser convertido.
     * @param disciplinaEntity A entidade JPA da disciplina associada.
     * @return A {@link MaterialEntity} correspondente.
     */
    public static MaterialEntity toEntity(Material material, DisciplinaEntity disciplinaEntity) {
        if (material == null) {
            return null;
        }

        return new MaterialEntity(
                material.getId(),
                material.getNomeOriginal(),
                material.getNomeStorage(),
                material.getTipoArquivo(),
                material.getTamanho(),
                material.getUsuarioId(),
                disciplinaEntity // Usa a entidade da disciplina já buscada
        );
    }

    /**
     * Converte uma entidade JPA {@link MaterialEntity} para um objeto de domínio {@link Material}.
     *
     * @param materialEntity A entidade JPA MaterialEntity a ser convertida.
     * @return O objeto de domínio {@link Material} correspondente, ou {@code null} se a entrada for {@code null}.
     */
    public static Material toDomain(MaterialEntity materialEntity) {
        if (materialEntity == null) {
            return null;
        }

        // A entidade JPA tem o objeto DisciplinaEntity completo, mas o domínio Material precisa apenas do ID.
        // Se a DisciplinaEntity for LAZY e não estiver carregada, materialEntity.getDisciplina() pode falhar.
        // É responsabilidade do serviço garantir que a entidade esteja carregada ou usar uma consulta que a traga.
        UUID disciplinaId = (materialEntity.getDisciplina() != null) ? materialEntity.getDisciplina().getId() : null;

        return Material.criar(
                materialEntity.getId(),
                materialEntity.getNomeOriginal(),
                materialEntity.getNomeStorage(),
                materialEntity.getTipoArquivo(),
                materialEntity.getTamanho(),
                materialEntity.getUsuarioId(),
                disciplinaId
        );
    }

    /**
     * Converte uma lista de entidades JPA {@link MaterialEntity} para uma lista de objetos de domínio {@link Material}.
     *
     * @param entities A lista de {@link MaterialEntity} a ser convertida.
     * @return Uma lista de {@link Material}, ou uma lista vazia se a entrada for {@code null} ou vazia.
     */
    public static List<Material> toDomainList(List<MaterialEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(MaterialMapper::toDomain)
                .collect(Collectors.toList());
    }

}
