package com.pdfocus.application.disciplina.dto;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Resumo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO (Data Transfer Object) que representa a visão detalhada de uma disciplina,
 * incluindo suas listas de resumos e materiais associados.
 * Este é o "dossier completo" retornado pela API para a página de detalhes.
 */
public record DetalheDisciplinaResponse(
        UUID id,
        String nome,
        String descricao,
        List<ResumoSimples> resumos,
        List<MaterialSimples> materiais
) {

    /**
     * DTO aninhado para representar um resumo de forma simplificada na lista.
     */
    public record ResumoSimples(UUID id, String titulo) {
        /**
         * Converte um objeto de domínio Resumo para o DTO ResumoSimples.
         */
        public static ResumoSimples fromDomain(Resumo resumo) {
            return new ResumoSimples(resumo.getId(), resumo.getTitulo());
        }
    }

    /**
     * DTO aninhado para representar um material de forma simplificada na lista.
     */
    public record MaterialSimples(UUID id, String nomeArquivo) {
        /**
         * Converte um objeto de domínio Material para o DTO MaterialSimples.
         */
        public static MaterialSimples fromDomain(Material material) {
            return new MaterialSimples(material.getId(), material.getNomeOriginal());
        }
    }

    /**
     * Método de fábrica para converter os objetos de domínio (Disciplina, Resumos, Materiais)
     * para este DTO de resposta completo.
     *
     * @param disciplina A entidade de domínio principal.
     * @param resumos A lista de resumos associados.
     * @param materiais A lista de materiais associados.
     * @return uma nova instância de DetalheDisciplinaResponse.
     */
    public static DetalheDisciplinaResponse fromDomain(
            Disciplina disciplina,
            List<Resumo> resumos,
            List<Material> materiais
    ) {
        // Converte a lista de domínios para a lista de DTOs simples
        List<ResumoSimples> resumosSimples = resumos.stream()
                .map(ResumoSimples::fromDomain)
                .collect(Collectors.toList());

        List<MaterialSimples> materiaisSimples = materiais.stream()
                .map(MaterialSimples::fromDomain)
                .collect(Collectors.toList());

        // Monta o DTO de resposta final
        return new DetalheDisciplinaResponse(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao(),
                resumosSimples,
                materiaisSimples
        );
    }
}

