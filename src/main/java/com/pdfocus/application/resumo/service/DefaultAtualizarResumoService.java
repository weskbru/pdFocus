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
 * Implementação padrão do caso de uso para atualizar um resumo existente.
 */
@Service
public class DefaultAtualizarResumoService implements AtualizarResumoUseCase {

    private final ResumoRepository resumoRepository;

    public DefaultAtualizarResumoService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * A operação é transacional. Ela busca o resumo pelo seu ID e pelo ID do usuário
     * para garantir a propriedade. Se encontrado, um novo objeto de domínio {@link Resumo}
     * é criado com os dados atualizados, o que revalida as regras de negócio,
     * e então é persistido.
     * </p>
     * @throws IllegalArgumentException se qualquer um dos parâmetros for nulo.
     */
    @Override
    @Transactional
    public Optional<Resumo> executar(UUID id, UUID usuarioId, AtualizarResumoCommand command) {
        // 1. Validação de entradas (Guard Clauses)
        Objects.requireNonNull(id, "O ID do resumo não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");
        Objects.requireNonNull(command, "O comando de atualização não pode ser nulo.");

        // 2. Busca o resumo, já validando se ele pertence ao usuário
        Optional<Resumo> resumoOptional = resumoRepository.buscarPorIdEUsuario(id, usuarioId);

        // Se não encontrou (ou não pertence ao usuário), retorna um Optional vazio
        if (resumoOptional.isEmpty()) {
            return Optional.empty();
        }

        // Pega o resumo existente
        Resumo resumoExistente = resumoOptional.get();

        // Cria uma nova instância de Resumo com os dados atualizados
        Resumo resumoAtualizado = Resumo.criar(
                resumoExistente.getId(),        // Mantem o mesmo ID
                resumoExistente.getUsuarioId(), // Mantem o mesmo usuario
                command.titulo(),               // Usa o novo título
                command.conteudo(),             // Usa o novo conteúdo
                resumoExistente.getDisciplina() // Mantem a mesma disciplina
        );

        // Salva o resumo atualizado
        Resumo resumoSalvo = resumoRepository.salvar(resumoAtualizado);

        // Retorna o resumo salvo
        return Optional.of(resumoSalvo);
    }
}
