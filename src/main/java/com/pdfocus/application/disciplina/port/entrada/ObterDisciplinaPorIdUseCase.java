package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta de entrada (caso de uso) responsável por obter os detalhes de uma disciplina
 * pertencente ao utilizador autenticado.
 * <p>
 * Este caso de uso agrega dados relacionados, como resumos e materiais da disciplina,
 * sendo a lista de materiais retornada de forma paginada.
 */
public interface ObterDisciplinaPorIdUseCase {

    /**
     * Executa a lógica de negócio para recuperar os detalhes completos de uma disciplina,
     * incluindo metadados, resumos e uma lista paginada de materiais.
     * <p>
     * A implementação deve garantir que apenas disciplinas pertencentes ao utilizador
     * autenticado sejam retornadas.
     *
     * @param id        O {@link UUID} da disciplina a ser consultada.
     * @param pageable  As informações de paginação (tamanho da página, número da página, etc.).
     * @return Um {@link Optional} contendo o {@link DetalheDisciplinaResponse} caso a disciplina
     *         exista e pertença ao utilizador autenticado; caso contrário, um Optional vazio.
     */
    Optional<DetalheDisciplinaResponse> executar(UUID id, Pageable pageable);
}
