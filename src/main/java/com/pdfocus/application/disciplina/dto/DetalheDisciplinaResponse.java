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
 * <p>
 * Inclui informações básicas da disciplina, seus resumos associados e uma
 * página de materiais relacionados. Essa estrutura facilita a exibição
 * de detalhes sem expor diretamente o modelo de domínio.
 */
public record DetalheDisciplinaResponse(
        UUID id,
        String nome,
        String descricao,
        List<ResumoSimples> resumos,
        Page<MaterialSimples> materiais
) {

    /**
     * DTO aninhado que representa um resumo em formato simplificado.
     * Usado para reduzir a carga de dados transferidos e desacoplar a entidade de domínio.
     *
     * @param id           Identificador único do resumo.
     * @param titulo       Título do resumo.
     * @param materialId   ID do material associado ao resumo.
     * @param dataCriacao  Data de criação do resumo em formato textual.
     */
    public record ResumoSimples(UUID id, String titulo, UUID materialId, String dataCriacao) {

        /**
         * Converte uma entidade de domínio {@link Resumo} para sua representação simplificada.
         *
         * @param resumo Entidade de domínio a ser convertida.
         * @return Uma instância de {@link ResumoSimples}.
         */
        public static ResumoSimples fromDomain(Resumo resumo) {
            return new ResumoSimples(
                    resumo.getId(),
                    resumo.getTitulo(),
                    resumo.getMaterialId(),
                    resumo.getDataCriacao().toString()
            );
        }
    }

    /**
     * DTO aninhado que representa um material em formato simplificado.
     *
     * @param id          Identificador único do material.
     * @param nomeArquivo Nome original do arquivo associado.
     */
    public record MaterialSimples(UUID id, String nomeArquivo) {

        /**
         * Converte uma entidade de domínio {@link Material} para sua representação simplificada.
         *
         * @param material Entidade de domínio a ser convertida.
         * @return Uma instância de {@link MaterialSimples}.
         */
        public static MaterialSimples fromDomain(Material material) {
            return new MaterialSimples(material.getId(), material.getNomeOriginal());
        }
    }

    /**
     * Constrói um {@link DetalheDisciplinaResponse} a partir das entidades de domínio.
     * <p>
     * Os resumos são convertidos para {@link ResumoSimples} e os materiais são mapeados
     * dentro de uma {@link Page} de {@link MaterialSimples}, preservando os metadados de paginação.
     *
     * @param disciplina        Entidade de domínio principal.
     * @param resumos           Lista de resumos associados à disciplina.
     * @param paginaDeMateriais Página de materiais associados à disciplina.
     * @return Uma instância de {@link DetalheDisciplinaResponse} pronta para resposta.
     */
    public static DetalheDisciplinaResponse fromDomain(
            Disciplina disciplina,
            List<Resumo> resumos,
            Page<Material> paginaDeMateriais
    ) {
        List<ResumoSimples> resumosSimples = resumos.stream()
                .map(ResumoSimples::fromDomain)
                .collect(Collectors.toList());

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
