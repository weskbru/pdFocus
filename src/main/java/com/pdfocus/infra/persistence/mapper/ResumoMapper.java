package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.ResumoEntity;

import java.util.List;
import java.util.stream.Collectors; // Adicionei este import que faltava no original

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

        // CORREÇÃO: Adicionar materialId no construtor (Esta correção já estava no original que você mandou)
        ResumoEntity entity = new ResumoEntity();
        entity.setId(resumo.getId());
        entity.setUsuarioId(resumo.getUsuarioId());
        entity.setTitulo(resumo.getTitulo());
        entity.setConteudo(resumo.getConteudo());
        entity.setDisciplina(disciplinaEntity);
        entity.setMaterialId(resumo.getMaterialId()); // NOVO CAMPO (Este campo já estava no original que você mandou)

        return entity;
    }

    /**
     * Converte uma entidade JPA {@link ResumoEntity} para um objeto de domínio {@link Resumo}.
     * (Versão ORIGINAL antes de adicionar dataCriacao)
     */
    public static Resumo toDomain(ResumoEntity resumoEntity) {
        if (resumoEntity == null) {
            return null;
        }

        Disciplina disciplina = null;
        if (resumoEntity.getDisciplina() != null) {
            // Assume que DisciplinaMapper.toDomain existe e funciona
            disciplina = DisciplinaMapper.toDomain(resumoEntity.getDisciplina());
        } else {
            // Tratamento de erro se necessário, mas mantendo a lógica original
            System.err.println("Aviso: ResumoEntity encontrada sem DisciplinaEntity associada (ID Resumo: " + resumoEntity.getId() + ")");
            // Ou lançar exceção dependendo da sua regra
        }


        // Lógica ORIGINAL que você tinha:
        // Verificar se é um resumo baseado em material ou manual
        if (resumoEntity.getMaterialId() != null) {
            // Usar o factory method para resumos baseados em material
            // (Este método Resumo.criarDeMaterial define a data como .now())
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
            // (Este método Resumo.criar também define a data como .now())
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
                .map(ResumoMapper::toDomain) // Usa o método toDomain original
                .collect(Collectors.toList());
    }

    /**
     * Converte uma lista de objetos de domínio {@link Resumo} para uma lista de entidades JPA {@link ResumoEntity}.
     */
    public static List<ResumoEntity> toEntityList(List<Resumo> domainObjects) {
        if (domainObjects == null || domainObjects.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return domainObjects.stream()
                .map(ResumoMapper::toEntity) // Usa o método toEntity original
                .collect(Collectors.toList());
    }
}