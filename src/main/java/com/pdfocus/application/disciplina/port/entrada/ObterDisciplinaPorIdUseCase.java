package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;
import org.springframework.data.domain.Pageable; // ✅ 1. ADICIONE ESTE IMPORT
import java.util.Optional;
import java.util.UUID;

public interface ObterDisciplinaPorIdUseCase {

    /**
     * Busca os detalhes de uma disciplina, incluindo uma lista paginada de seus materiais.
     * @param id O UUID da disciplina.
     * @param pageable As informações de paginação. //
     * @return Um Optional com os detalhes da disciplina, se encontrada e pertencente ao usuário.
     */
    Optional<DetalheDisciplinaResponse> executar(UUID id, Pageable pageable);
}