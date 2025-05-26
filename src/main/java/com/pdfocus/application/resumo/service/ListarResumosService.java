package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.entrada.ListarResumosUseCase;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.models.Resumo;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para listar resumos.
 * Este serviço utiliza um {@link ResumoRepository} para acessar os dados dos resumos.
 */
public class ListarResumosService implements ListarResumosUseCase {

    private final ResumoRepository resumoRepository;

    /**
     * Constrói uma nova instância de {@code ListarResumosService}.
     *
     * @param resumoRepository O repositório a ser usado para buscar os resumos. Não pode ser {@code null}.
     * @throws NullPointerException se {@code resumoRepository} for {@code null}.
     */
    public ListarResumosService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * Busca todos os resumos pertencentes ao usuário especificado.
     *
     * @param usuarioId O ID do usuário para o qual os resumos serão listados.
     * @return Uma lista de {@link Resumo} pertencentes ao usuário.
     * @throws CampoNuloException se {@code usuarioId} for {@code null}.
     */
    @Override
    public List<Resumo> buscarTodosPorUsuario(UUID usuarioId) {
        if (usuarioId == null) {
            // Mensagem da exceção corrigida
            throw new CampoNuloException("O ID do usuário não pode ser nulo para listar os resumos.");
        }
        return resumoRepository.buscarTodosPorUsuario(usuarioId);
    }

    /**
     * {@inheritDoc}
     * Busca todos os resumos pertencentes ao usuário especificado e associados à disciplina informada.
     *
     * @param disciplinaId O ID da disciplina pela qual os resumos serão filtrados.
     * @param usuarioId O ID do usuário para o qual os resumos serão listados.
     * @return Uma lista de {@link Resumo} filtrados por disciplina e pertencentes ao usuário.
     * @throws CampoNuloException se {@code disciplinaId} ou {@code usuarioId} forem {@code null}.
     */
    @Override
    public List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId) {
        if (disciplinaId == null) {
            throw new CampoNuloException("O ID da disciplina não pode ser nulo para listar os resumos.");
        }
        if (usuarioId == null) {
            throw new CampoNuloException("O ID do usuário não pode ser nulo para listar os resumos.");
        }
        return resumoRepository.buscarPorDisciplinaEUsuario(disciplinaId, usuarioId);
    }
}