package com.pdfocus.application.resumo.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.port.entrada.CriarResumoUseCase;
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Caso de uso responsável pela criação de novos resumos no sistema.
 * <p>
 * Implementa o contrato {@link CriarResumoUseCase}, orquestrando a interação entre
 * as camadas de aplicação e domínio. Garante que a disciplina informada exista antes
 * de persistir o resumo, mantendo a consistência das regras de negócio.
 * </p>
 *
 * <h2>Fluxo de execução</h2>
 * <ol>
 *     <li>Valida o comando de entrada e o usuário autor.</li>
 *     <li>Verifica a existência da disciplina associada.</li>
 *     <li>Cria a entidade {@link Resumo} via método de fábrica do domínio.</li>
 *     <li>Persiste o resumo usando {@link ResumoRepository}.</li>
 * </ol>
 *
 * <p>
 * Essa classe é anotada com {@link Service} e {@link Transactional},
 * garantindo que a operação seja atômica: ou tudo é salvo, ou nada é persistido.
 * </p>
 *
 * @see com.pdfocus.application.resumo.dto.CriarResumoCommand
 * @see com.pdfocus.core.models.Resumo
 * @see com.pdfocus.application.resumo.port.saida.ResumoRepository
 * @see com.pdfocus.application.disciplina.port.saida.DisciplinaRepository
 */
@Service
public class DefaultCriarResumoService implements CriarResumoUseCase {

    private final ResumoRepository resumoRepository;
    private final DisciplinaRepository disciplinaRepository;

    /**
     * Cria uma instância do serviço de criação de resumos.
     *
     * @param resumoRepository       Repositório responsável pela persistência de {@link Resumo}.
     * @param disciplinaRepository   Repositório responsável pela busca de {@link Disciplina}.
     */
    public DefaultCriarResumoService(ResumoRepository resumoRepository, DisciplinaRepository disciplinaRepository) {
        this.resumoRepository = resumoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * Executa o caso de uso de criação de resumo.
     * <p>
     * O método valida o comando, garante que a disciplina existe, cria a entidade
     * {@link Resumo} e delega sua persistência ao repositório.
     * </p>
     *
     * @param command   Objeto com os dados de criação do resumo. Não pode ser nulo.
     * @param usuarioId Identificador do usuário autor. Não pode ser nulo.
     * @return O resumo recém-criado e persistido.
     * @throws IllegalArgumentException           se {@code command} ou {@code usuarioId} forem nulos.
     * @throws DisciplinaNaoEncontradaException   se a disciplina informada no comando não existir.
     */
    @Override
    @Transactional
    public Resumo executar(CriarResumoCommand command, UUID usuarioId) {
        Objects.requireNonNull(command, "O comando de criação não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        // 1. Verifica se a disciplina existe E PERTENCE AO USUÁRIO
        Disciplina disciplina = disciplinaRepository.findByIdAndUsuarioId(command.disciplinaId(), usuarioId)
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(command.disciplinaId()));

        // 2. Cria o objeto de domínio Resumo (validação automática via factory method)
        Resumo novoResumo = Resumo.criar(
                UUID.randomUUID(),
                usuarioId,
                command.titulo(),
                command.conteudo(),
                disciplina
        );

        // 3. Persiste e retorna o resumo criado
        return resumoRepository.salvar(novoResumo);
    }
}
