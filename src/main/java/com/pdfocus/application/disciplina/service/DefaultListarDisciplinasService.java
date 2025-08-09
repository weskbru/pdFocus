package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.ListarDisciplinasUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para listar as disciplinas de um usuário.
 * Este serviço orquestra a validação da entrada e delega a busca ao repositório.
 */
@Service
public class DefaultListarDisciplinasService implements ListarDisciplinasUseCase {

    private final DisciplinaRepository disciplinaRepository;

    /**
     * Constrói o serviço com a dependência do repositório de disciplinas.
     *
     * @param disciplinaRepository A porta de saída para a persistência de disciplinas.
     */
    public DefaultListarDisciplinasService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository, "DisciplinaRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Este metodo executa a busca de forma transacional e somente leitura para otimização.
     * Ele valida se o ID do usuário não é nulo antes de delegar a chamada para o repositório.
     * </p>
     *
     * @param usuarioId O identificador único do usuário cujas disciplinas serão listadas.
     * @return Uma lista de {@link Disciplina} pertencentes ao usuário.
     * @throws IllegalArgumentException se o {@code usuarioId} for nulo.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Disciplina> executar(UUID usuarioId) {
        // Valida a pré-condição do caso de uso: o ID do usuário não pode ser nulo.
        Objects.requireNonNull(usuarioId, "ID do usuário não pode ser nulo.");

        // Chama o metodo correto do repositório que busca por usuário.
        return disciplinaRepository.listaTodasPorUsuario(usuarioId);
    }
}
