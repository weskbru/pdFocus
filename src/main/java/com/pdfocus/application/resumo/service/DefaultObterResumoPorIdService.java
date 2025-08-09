package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.entrada.ObterResumoPorIdUseCase;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Resumo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para obter um resumo por seu ID e ID do usuário.
 */
@Service
public class DefaultObterResumoPorIdService implements ObterResumoPorIdUseCase {

    private final ResumoRepository resumoRepository;

    public DefaultObterResumoPorIdService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * A busca é transacional e somente leitura para otimização.
     * </p>
     * @throws IllegalArgumentException se o {@code id} ou {@code usuarioId} forem nulos.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Resumo> executar(UUID id, UUID usuarioId) {
        // Validação de entradas (Guard Clauses)
        Objects.requireNonNull(id, "O ID do resumo não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        // Delega a busca para o repositório, que já tem a lógica de buscar
        // combinando o ID do resumo e o ID do usuário.
        return resumoRepository.buscarPorIdEUsuario(id, usuarioId);
    }
}