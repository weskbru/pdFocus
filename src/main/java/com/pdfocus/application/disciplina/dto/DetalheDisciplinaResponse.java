package com.pdfocus.application.disciplina.dto;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Resumo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO que representa a visão detalhada de uma disciplina.
 * Agora, a lista de materiais é paginada.
 */
public record DetalheDisciplinaResponse(
        UUID id,
        String nome,
        String descricao,
        List<ResumoSimples> resumos,
        Page<MaterialSimples> materiais
) {

    /**
     * DTO aninhado para representar um resumo de forma simplificada.
     */
    public record ResumoSimples(UUID id, String titulo, UUID materialId, String dataCriacao) {
        public static ResumoSimples fromDomain(Resumo resumo) {
            // Agora estas chamadas são válidas
            return new ResumoSimples(resumo.getId(), resumo.getTitulo(), resumo.getMaterialId(), resumo.getDataCriacao().toString());
        }
    }

    /**
     * DTO aninhado para representar um material de forma simplificada.
     */
    public record MaterialSimples(UUID id, String nomeArquivo) {
        public static MaterialSimples fromDomain(Material material) {
            return new MaterialSimples(material.getId(), material.getNomeOriginal());
        }
    }

    /**
     * Constrói o DTO a partir dos modelos de domínio, agora aceitando uma página de materiais.
     *
     * @param disciplina A entidade de domínio principal.
     * @param resumos A lista de resumos associados.
     * @param paginaDeMateriais A PÁGINA de materiais associados.
     * @return uma nova instância de DetalheDisciplinaResponse.
     */
    public static DetalheDisciplinaResponse fromDomain(
            Disciplina disciplina,
            List<Resumo> resumos,
            Page<Material> paginaDeMateriais // O parâmetro agora é um Page<Material>
    ) {
        // A conversão da lista de resumos continua igual
        List<ResumoSimples> resumosSimples = resumos.stream()
                .map(ResumoSimples::fromDomain)
                .collect(Collectors.toList());


        // O método .map() da interface Page é usado para converter o conteúdo da página
        // de Material para MaterialSimples, preservando todos os metadados de paginação.
        Page<MaterialSimples> materiaisSimples = paginaDeMateriais.map(MaterialSimples::fromDomain);

        return new DetalheDisciplinaResponse(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao(),
                resumosSimples,
                materiaisSimples
        );
    }
}