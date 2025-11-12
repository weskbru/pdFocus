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
 * Implementação padrão do caso de uso {@link ListarResumosUseCase}.
 * <p>
 * Este serviço é responsável por orquestrar a recuperação de {@link Resumo}s
 * com base em critérios de busca (usuário e/ou disciplina), garantindo
 * a integridade e segurança dos dados consultados.
 * </p>
 * <p>
 * Todas as operações são executadas em contexto transacional somente-leitura
 * para otimização de desempenho e isolamento das transações.
 * </p>
 */
@Service
public class DefaultListarResumosService implements ListarResumosUseCase {

    private final ResumoRepository resumoRepository;

    /**
     * Constrói o serviço com o repositório responsável pela leitura de {@link Resumo}s.
     *
     * @param resumoRepository A porta de saída para acesso aos dados de resumos.
     */
    public DefaultListarResumosService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retorna todos os resumos pertencentes a um usuário específico.
     * A operação é transacional e somente leitura.
     * </p>
     *
     * @param usuarioId ID do usuário autenticado.
     * @return Lista de {@link Resumo}s pertencentes ao usuário.
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
     * Retorna todos os resumos de uma disciplina específica pertencentes ao usuário autenticado.
     * A operação é transacional e somente leitura.
     * </p>
     *
     * @param disciplinaId ID da disciplina a ser filtrada.
     * @param usuarioId    ID do usuário autenticado.
     * @return Lista de {@link Resumo}s da disciplina filtrada.
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
