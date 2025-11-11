package com.pdfocus.application.disciplina.dto;

import com.pdfocus.core.models.Disciplina;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) que representa os dados públicos de uma {@link Disciplina}
 * expostos pela camada de aplicação.
 * <p>
 * Usado em respostas de API para listar ou exibir informações básicas de uma disciplina,
 * garantindo o isolamento entre o domínio e o mundo externo (frontend ou consumidores externos).
 */
public record DisciplinaResponse(
        UUID id,
        String nome,
        String descricao
) {

    /**
     * Converte uma entidade de domínio {@link Disciplina} em um {@link DisciplinaResponse}.
     * <p>
     * Este método atua como um <b>factory method</b> (método de fábrica), encapsulando a lógica
     * de mapeamento e assegurando que apenas dados necessários sejam expostos pela API.
     *
     * @param disciplina Entidade de domínio que representa a disciplina.
     * @return Uma instância de {@link DisciplinaResponse} com os dados convertidos.
     */
    public static DisciplinaResponse fromDomain(Disciplina disciplina) {
        return new DisciplinaResponse(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao()
        );
    }
}
