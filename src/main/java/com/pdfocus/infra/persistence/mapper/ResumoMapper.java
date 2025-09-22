package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.ResumoEntity;

import java.util.List;

public class ResumoMapper {

    private ResumoMapper() {
        // Impede a instanciação
    }

    /**
     * Converte um objeto de domínio {@link Resumo} para uma entidade JPA {@link ResumoEntity}.
     */
    public static ResumoEntity toEntity(Resumo resumo) {
        if (resumo == null) {
            return null;
        }

        DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(resumo.getDisciplina());

        // CORREÇÃO: Adicionar materialId no construtor
        ResumoEntity entity = new ResumoEntity();
        entity.setId(resumo.getId());
        entity.setUsuarioId(resumo.getUsuarioId());
        entity.setTitulo(resumo.getTitulo());
        entity.setConteudo(resumo.getConteudo());
        entity.setDisciplina(disciplinaEntity);
        entity.setMaterialId(resumo.getMaterialId()); // NOVO CAMPO

        return entity;
    }

    /**
     * Converte uma entidade JPA {@link ResumoEntity} para um objeto de domínio {@link Resumo}.
     */
    public static Resumo toDomain(ResumoEntity resumoEntity) {
        if (resumoEntity == null) {
            return null;
        }

        Disciplina disciplina = null;
        if (resumoEntity.getDisciplina() != null) {
            disciplina = DisciplinaMapper.toDomain(resumoEntity.getDisciplina());
        }

        // CORREÇÃO: Verificar se é um resumo baseado em material ou manual
        if (resumoEntity.getMaterialId() != null) {
            // Usar o novo factory method para resumos baseados em material
            return Resumo.criarDeMaterial(
                    resumoEntity.getId(),
                    resumoEntity.getUsuarioId(),
                    resumoEntity.getTitulo(),
                    resumoEntity.getConteudo(),
                    disciplina,
                    resumoEntity.getMaterialId()
            );
        } else {
            // Usar o factory method original para resumos manuais
            return Resumo.criar(
                    resumoEntity.getId(),
                    resumoEntity.getUsuarioId(),
                    resumoEntity.getTitulo(),
                    resumoEntity.getConteudo(),
                    disciplina
            );
        }
    }

    /**
     * Converte uma lista de entidades JPA {@link ResumoEntity} para uma lista de objetos de domínio {@link Resumo}.
     */
    public static List<Resumo> toDomainList(List<ResumoEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return entities.stream()
                .map(ResumoMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Converte uma lista de objetos de domínio {@link Resumo} para uma lista de entidades JPA {@link ResumoEntity}.
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