package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DisciplinaMapper {

    private DisciplinaMapper() {
        // Impede a instanciação
    }

    /**
     * Converte um objeto de domínio {@link Disciplina} para uma entidade JPA {@link DisciplinaEntity}.
     */
    public static DisciplinaEntity toEntity(Disciplina disciplina) {
        if (disciplina == null) {
            return null;
        }

        return new DisciplinaEntity(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao(),
                disciplina.getUsuarioId() // <-- MUDANÇA AQUI
        );
    }

    /**
     * Converte uma entidade JPA {@link DisciplinaEntity} para um objeto de domínio {@link Disciplina}.
     */
    public static Disciplina toDomain(DisciplinaEntity disciplinaEntity) {
        if (disciplinaEntity == null) {
            return null;
        }

        return new Disciplina(
                disciplinaEntity.getId(),
                disciplinaEntity.getNome(),
                disciplinaEntity.getDescricao(),
                disciplinaEntity.getUsuarioId() // <-- MUDANÇA AQUI
        );
    }

    /**
     * Converte uma lista de entidades JPA {@link DisciplinaEntity} para uma lista de objetos de domínio {@link Disciplina}.
     */
    public static List<Disciplina> toDomainList(List<DisciplinaEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(DisciplinaMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Converte uma lista de objetos de domínio {@link Disciplina} para uma lista de entidades JPA {@link DisciplinaEntity}.
     * (Menos comum de ser necessário no fluxo de saída do repositório, mas pode ser útil em outros cenários)
     *
     * @param domainObjects A lista de {@link Disciplina} a ser convertida.
     * @return Uma lista de {@link DisciplinaEntity}, ou uma lista vazia se a entrada for {@code null} ou vazia.
     */
    public static List<DisciplinaEntity> toEntityList(List<Disciplina> domainObjects) {
        if (domainObjects == null) {
            return Collections.emptyList(); // Ou null
        }
        return domainObjects.stream()
                .map(DisciplinaMapper::toEntity)
                .collect(Collectors.toList());
    }
}
