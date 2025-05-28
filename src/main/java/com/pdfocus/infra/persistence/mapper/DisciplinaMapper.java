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
     *
     * @param disciplina O objeto de domínio Disciplina a ser convertido.
     * @return A {@link DisciplinaEntity} correspondente, ou {@code null} se a entrada for {@code null}.
     */
    public static DisciplinaEntity toEntity(Disciplina disciplina) {
        if (disciplina == null) {
            return null;
        }

        // Cria uma nova instância da Disciplina (domínio) usando construtor público,
        // que já contém as validações de campos (id, nome, descricao).
        return new DisciplinaEntity(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao()
        );
    }

    /**
     * Converte uma entidade JPA {@link DisciplinaEntity} para um objeto de domínio {@link Disciplina}.
     *
     * @param disciplinaEntity A entidade JPA DisciplinaEntity a ser convertida.
     * @return O objeto de domínio {@link Disciplina} correspondente, ou {@code null} se a entrada for {@code null}.
     */
    public static Disciplina toDomain(DisciplinaEntity disciplinaEntity) {
        if (disciplinaEntity == null) {
            return null;
        }
        // Chama diretamente o CONSTRUTOR PÚBLICO da classe de domínio Disciplina
        return new Disciplina(
                disciplinaEntity.getId(),
                disciplinaEntity.getNome(),
                disciplinaEntity.getDescricao()
        );


    }

    /**
     * Converte uma lista de entidades JPA {@link DisciplinaEntity} para uma lista de objetos de domínio {@link Disciplina}.
     *
     * @param entities A lista de {@link DisciplinaEntity} a ser convertida.
     * @return Uma lista de {@link Disciplina}, ou uma lista vazia se a entrada for {@code null} ou vazia.
     */
    public static List<Disciplina> toDomainList(List<DisciplinaEntity> entities) {
        if (entities == null) {
            return Collections.emptyList(); // Ou null, dependendo da sua preferência de contrato
        }
        return entities.stream()
                .map(DisciplinaMapper::toDomain) // Reutiliza o método de mapeamento individual
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
