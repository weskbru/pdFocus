package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.entrada.DeletarResumoUseCase;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.resumo.ResumoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso {@link DeletarResumoUseCase}.
 * <p>
 * Este serviço é responsável por orquestrar a exclusão de um {@link com.pdfocus.core.models.Resumo}
 * garantindo que ele pertença ao usuário autenticado. A operação é executada de forma segura
 * e transacional — ou seja, caso ocorra qualquer falha durante o processo, a deleção é revertida.
 * </p>
 */
@Service
public class DefaultDeletarResumoService implements DeletarResumoUseCase {

    private final ResumoRepository resumoRepository;

    /**
     * Constrói o serviço com o repositório responsável pela persistência de {@link com.pdfocus.core.models.Resumo}.
     *
     * @param resumoRepository Repositório de acesso e manipulação de resumos.
     */
    public DefaultDeletarResumoService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * A operação valida os parâmetros de entrada e delega a exclusão ao repositório,
     * que se encarrega de verificar a propriedade do resumo e lançar exceção apropriada.
     * </p>
     *
     * @param id         ID do resumo a ser deletado.
     * @param usuarioId  ID do usuário proprietário do resumo.
     * @throws ResumoNaoEncontradoException se o resumo não existir ou não pertencer ao usuário.
     * @throws IllegalArgumentException se qualquer parâmetro for nulo.
     */
    @Override
    @Transactional
    public void executar(UUID id, UUID usuarioId) {
        Objects.requireNonNull(id, "O ID do resumo não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        resumoRepository.deletarPorIdEUsuario(id, usuarioId);
    }
}
