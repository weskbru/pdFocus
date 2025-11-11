package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.dto.AtualizarResumoCommand;
import com.pdfocus.application.resumo.port.entrada.AtualizarResumoUseCase;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Resumo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso {@link AtualizarResumoUseCase}.
 * <p>
 * Este serviço aplica as regras de negócio necessárias para atualizar um
 * {@link Resumo} existente, garantindo que ele pertença ao usuário autenticado.
 * </p>
 *
 * <p>
 * O processo consiste em:
 * <ol>
 *   <li>Validar os parâmetros recebidos;</li>
 *   <li>Buscar o resumo pelo ID e ID do usuário;</li>
 *   <li>Recriar o objeto de domínio com os novos dados, revalidando as invariantes;</li>
 *   <li>Persistir o resumo atualizado.</li>
 * </ol>
 * </p>
 */
@Service
public class DefaultAtualizarResumoService implements AtualizarResumoUseCase {

    private final ResumoRepository resumoRepository;

    /**
     * Constrói o serviço com o repositório necessário para persistir as alterações.
     *
     * @param resumoRepository O repositório de {@link Resumo}.
     */
    public DefaultAtualizarResumoService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * A operação é executada de forma transacional: caso qualquer etapa falhe,
     * todas as alterações são revertidas automaticamente.
     * </p>
     *
     * @param id         ID do resumo a ser atualizado.
     * @param usuarioId  ID do usuário proprietário.
     * @param command    Objeto contendo os novos dados do resumo.
     * @return Um {@link Optional} contendo o resumo atualizado, ou vazio caso o resumo
     *         não exista ou não pertença ao usuário.
     * @throws IllegalArgumentException se qualquer parâmetro for nulo.
     */
    @Override
    @Transactional
    public Optional<Resumo> executar(UUID id, UUID usuarioId, AtualizarResumoCommand command) {
        Objects.requireNonNull(id, "O ID do resumo não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");
        Objects.requireNonNull(command, "O comando de atualização não pode ser nulo.");

        Optional<Resumo> resumoOptional = resumoRepository.buscarPorIdEUsuario(id, usuarioId);
        if (resumoOptional.isEmpty()) return Optional.empty();

        Resumo resumoExistente = resumoOptional.get();

        // Recria a entidade com os novos dados (mantendo integridade e imutabilidade)
        Resumo resumoAtualizado = Resumo.criar(
                resumoExistente.getId(),
                resumoExistente.getUsuarioId(),
                command.titulo(),
                command.conteudo(),
                resumoExistente.getDisciplina()
        );

        return Optional.of(resumoRepository.salvar(resumoAtualizado));
    }
}
