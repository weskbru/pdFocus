package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.AtualizarDisciplinaUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para atualizar uma disciplina.
 * Este serviço orquestra a busca, validação e persistência da disciplina atualizada.
 */
@Service
public class DefaultAtualizarDisciplinaService implements AtualizarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    /**
     * Constrói o serviço com a dependência do repositório de disciplinas.
     *
     * @param disciplinaRepository A porta de saída para a persistência de disciplinas.
     */
    public DefaultAtualizarDisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Este método executa a atualização de uma disciplina em uma única transação.
     * Ele primeiro busca a disciplina existente pelo seu ID. Se encontrada, cria uma nova
     * instância de domínio com os dados atualizados (o que aciona as validações
     * do domínio) e então a persiste.
     * </p>
     *
     * @param id O ID da disciplina a ser atualizada. Não pode ser nulo.
     * @param command O comando contendo os novos dados. Não pode ser nulo.
     * @return Um {@link Optional} contendo a {@link Disciplina} atualizada se a operação
     * foi bem-sucedida, ou um Optional vazio se a disciplina original não foi encontrada.
     * @throws IllegalArgumentException se o ID ou o comando forem nulos.
     */
    @Override
    @Transactional
    public Optional<Disciplina> executar(UUID id, AtualizarDisciplinaCommand command, UUID usuarioId) { // <-- Assinatura atualizada
        Objects.requireNonNull(id, "ID da disciplina não pode ser nulo.");
        Objects.requireNonNull(command, "Comando de atualização não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "ID do usuário não pode ser nulo.");

        // Busca a disciplina garantindo que ela pertence ao usuário logado
        return disciplinaRepository.findByIdAndUsuarioId(id, usuarioId)
                .map(disciplinaExistente -> {
                    // Se encontrou, cria uma nova instância com os dados atualizados
                    Disciplina disciplinaAtualizada = new Disciplina(
                            disciplinaExistente.getId(),
                            command.nome(),
                            command.descricao(),
                            usuarioId // Mantem o mesmo usuarioId
                    );
                    // Salva a disciplina atualizada
                    return disciplinaRepository.salvar(disciplinaAtualizada);
                });
    }
}
