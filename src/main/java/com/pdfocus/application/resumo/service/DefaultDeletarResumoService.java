package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.entrada.DeletarResumoUseCase;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para deletar um resumo.
 */
@Service
public class DefaultDeletarResumoService implements DeletarResumoUseCase {

    private final ResumoRepository resumoRepository;

    public DefaultDeletarResumoService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * A operação é transacional. Valida as entradas e delega a operação de
     * deleção segura (por ID do resumo e do usuário) para o repositório.
     * </p>
     * @throws IllegalArgumentException se qualquer um dos IDs for nulo.
     */
    @Override
    @Transactional
    public boolean executar(UUID id, UUID usuarioId) {
        // Validação de entradas (Guard Clauses)
        Objects.requireNonNull(id, "O ID do resumo não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        // Delega a chamada para o repositório, que já retorna um boolean
        return resumoRepository.deletarPorIdEUsuario(id, usuarioId);
    }
}
