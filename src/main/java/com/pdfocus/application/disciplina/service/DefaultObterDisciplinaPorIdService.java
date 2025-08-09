package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.ObterDisciplinaPorIdUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para obter uma disciplina por seu ID.
 * Este serviço orquestra a validação da entrada e delega a busca ao repositório.
 */
@Service
public class DefaultObterDisciplinaPorIdService implements ObterDisciplinaPorIdUseCase {
    private final DisciplinaRepository disciplinaRepository;

    /**
     * Constrói o serviço com a dependência do repositório de disciplinas.
     *
     * @param disciplinaRepository A porta de saída para a persistência de disciplinas.
     */
    public DefaultObterDisciplinaPorIdService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Este método executa a busca de uma disciplina de forma transacional e read-only,
     * o que pode otimizar a performance da consulta. Ele primeiro valida se o ID
     * fornecido não é nulo e depois delega a busca para o repositório.
     * </p>
     *
     * @param id O identificador único da disciplina a ser buscada. Não pode ser nulo.
     * @return Um {@link Optional} contendo a {@link Disciplina} se encontrada,
     * ou um Optional vazio caso contrário.
     * @throws IllegalArgumentException se o {@code id} fornecido for nulo.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> executar(UUID id, UUID usuarioId) { // <-- Assinatura atualizada
        Objects.requireNonNull(id, "ID da disciplina não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "ID do usuário não pode ser nulo.");

        // NOTA: Precisaremos de um novo método no repositório para isso.
        // Vamos adicioná-lo em seguida.
        return disciplinaRepository.findByIdAndUsuarioId(id, usuarioId);
    }
}
