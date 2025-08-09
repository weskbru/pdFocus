package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.DeletarDisciplinaUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para deletar uma disciplina.
 * Orquestra a validação da entrada e delega a operação de exclusão.
 */
@Service
public class DefaultDeletarDisciplinaService implements DeletarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public DefaultDeletarDisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Executa a deleção de uma disciplina de forma transacional.
     * Primeiro valida se o ID fornecido não é nulo e depois delega a chamada
     * para o repositório.
     * </p>
     *
     * @param id O identificador único da disciplina a ser deletada.
     * @throws IllegalArgumentException se o ID fornecido for nulo.
     * @throws com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException (ou similar)
     * se o repositório não encontrar a disciplina para deletar.
     */
    @Override
    @Transactional
    public void executar(UUID id, UUID usuarioId) {
        Objects.requireNonNull(id, "ID da disciplina para deleção não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "ID do usuário não pode ser nulo.");

        // Chama o método seguro que deleta apenas se o ID da disciplina E o ID do usuário corresponderem
        disciplinaRepository.deletarPorIdEUsuario(id, usuarioId);
    }
}