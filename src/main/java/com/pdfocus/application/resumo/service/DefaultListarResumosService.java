package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.entrada.ListarResumosUseCase;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Resumo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para listar resumos com base em critérios.
 * Este serviço orquestra a validação dos parâmetros e delega a busca ao repositório.
 */
@Service
public class DefaultListarResumosService implements ListarResumosUseCase {

    private final ResumoRepository resumoRepository;

    /**
     * Constrói o serviço com a dependência do repositório de resumos.
     *
     * @param resumoRepository A porta de saída para a persistência de resumos.
     */
    public DefaultListarResumosService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * A busca é transacional e somente leitura para otimização.
     * </p>
     * @throws IllegalArgumentException se o {@code usuarioId} for nulo.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Resumo> buscarTodosPorUsuario(UUID usuarioId) {
        Objects.requireNonNull(usuarioId, "ID do usuário não pode ser nulo.");
        return resumoRepository.buscarTodosPorUsuario(usuarioId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * A busca é transacional e somente leitura para otimização.
     * </p>
     * @throws IllegalArgumentException se {@code disciplinaId} ou {@code usuarioId} forem nulos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId) {
        Objects.requireNonNull(disciplinaId, "ID da disciplina não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "ID do usuário não pode ser nulo.");
        return resumoRepository.buscarPorDisciplinaEUsuario(disciplinaId, usuarioId);
    }
}
