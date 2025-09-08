package com.pdfocus.application.disciplina.dto;

import com.pdfocus.core.models.Disciplina;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) para representar os dados de uma Disciplina
 * que são enviados como resposta pela API.
 * <p>
 * Este é o "prato final" que o cliente (frontend) recebe, contendo
 * apenas os dados públicos e necessários para a visualização.
 */
public record DisciplinaResponse(
        UUID id,
        String nome,
        String descricao
) {

    /**
     * Método de fábrica (factory method) para converter um objeto de domínio
     * {@link Disciplina} para este DTO de resposta.
     * <p>
     * Esta é a forma padrão e segura de construir a resposta da API,
     * garantindo que apenas os campos desejados sejam expostos.
     *
     * @param disciplina O objeto de domínio a ser convertido.
     * @return uma nova instância de DisciplinaResponse.
     */
    public static DisciplinaResponse fromDomain(Disciplina disciplina) {
        return new DisciplinaResponse(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao()
        );
    }
}
