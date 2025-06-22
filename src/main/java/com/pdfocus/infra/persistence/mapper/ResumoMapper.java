package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.ResumoEntity;

import java.util.List;

public class ResumoMapper {

    /**
     * Construtor privado para impedir a instanciação da classe utilitária.
     */
    private ResumoMapper() {
        // Impede a instanciação
    }

    /**
     * Converte um objeto de domínio {@link Resumo} para uma entidade JPA {@link ResumoEntity}.
     * A disciplina associada também é convertida usando {@link DisciplinaMapper}.
     *
     * @param resumo O objeto de domínio Resumo a ser convertido.
     * @return A {@link ResumoEntity} correspondente, ou {@code null} se a entrada for {@code null}.
     */
    public static ResumoEntity toEntity(Resumo resumo) {
        if (resumo == null) {
            return null;
        }

        // Converte a Disciplina (domínio) para DisciplinaEntity (JPA)
        DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(resumo.getDisciplina());

        return new ResumoEntity(
                resumo.getId(),
                resumo.getUsuarioId(),
                resumo.getTitulo(),
                resumo.getConteudo(),
                disciplinaEntity // Passa a DisciplinaEntity convertida
        );
    }

    /**
     * Converte uma entidade JPA {@link ResumoEntity} para um objeto de domínio {@link Resumo}.
     * A disciplina associada também é convertida usando {@link DisciplinaMapper}.
     *
     * @param resumoEntity A entidade JPA ResumoEntity a ser convertida.
     * @return O objeto de domínio {@link Resumo} correspondente, ou {@code null} se a entrada for {@code null}.
     */
    public static Resumo toDomain(ResumoEntity resumoEntity) {
        if (resumoEntity == null) {
            return null;
        }

        Disciplina disciplina = null;
        if (resumoEntity.getDisciplina() != null) {
            disciplina = DisciplinaMapper.toDomain(resumoEntity.getDisciplina());
        }

        // Usa o metodo estático da classe de domínio Resumo
        return Resumo.criar(
                resumoEntity.getId(),
                resumoEntity.getUsuarioId(),
                resumoEntity.getTitulo(),
                resumoEntity.getConteudo(),
                disciplina // Passa a Disciplina (domínio) convertida
        );
    }

    /**
     * Converte uma lista de entidades JPA {@link ResumoEntity} para uma lista de objetos de domínio {@link Resumo}.
     *
     * @param entities A lista de {@link ResumoEntity} a ser convertida.
     * @return Uma lista de {@link Resumo}, ou uma lista vazia se a entrada for {@code null} ou vazia.
     */
    public static List<Resumo> toDomainList(List<ResumoEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return entities.stream()
                .map(ResumoMapper::toDomain) // Reutiliza o método de mapeamento individual que você já tem
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Converte uma lista de objetos de domínio {@link Resumo} para uma lista de entidades JPA {@link ResumoEntity}.
     *
     * @param domainObjects A lista de {@link Resumo} a ser convertida.
     * @return Uma lista de {@link ResumoEntity}, ou uma lista vazia se a entrada for {@code null} ou vazia.
     */
    public static List<ResumoEntity> toEntityList(List<Resumo> domainObjects) {
        if (domainObjects == null || domainObjects.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return domainObjects.stream()
                .map(ResumoMapper::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }


}
