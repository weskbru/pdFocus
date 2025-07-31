package com.pdfocus.application.disciplina.service;


import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.CriarDisciplinaUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementação da interface {@link CriarDisciplinaUseCase}.
 * Responsável por criar uma nova {@link Disciplina} e persistí-la
 * utilizando o {@link DisciplinaRepository}.
 */
@Service
public class DefaultCriarDisciplinaService implements CriarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    /**
     * Construtor que recebe uma instância de {@link DisciplinaRepository}
     * para realizar a persistência das disciplinas.
     *
     * @param disciplinaRepository O repositório de disciplinas.
     */
    public DefaultCriarDisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * Cria uma nova {@link Disciplina} a partir do {@link CriarDisciplinaCommand}
     * e a salva utilizando o {@link DisciplinaRepository}.
     *
     * @param command O comando contendo os dados para a criação da disciplina.
     * @return A {@link Disciplina} criada e salva.
     */
    @Override
    @Transactional
    public Disciplina executar(CriarDisciplinaCommand command, UUID usuarioId) { // <-- Assinatura atualizada
        Objects.requireNonNull(command, "O comando de criação não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        // Cria o objeto de domínio Disciplina, agora com o ID do usuário
        Disciplina novaDisciplina = new Disciplina(
                UUID.randomUUID(),
                command.nome(),
                command.descricao(),
                usuarioId // <-- Usa o ID do usuário autenticado
        );

        return disciplinaRepository.salvar(novaDisciplina);
    }
}