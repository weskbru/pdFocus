package com.pdfocus.application.resumo.service;


import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.port.entrada.CriarResumoUseCase;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import java.util.UUID;

/**
 * Serviço responsável por criar um novo resumo no sistema.
 *
 * Recebe dados através do comando {@link CriarResumoCommand}, valida a existência da disciplina,
 * cria a entidade Resumo e persiste usando o repositório.
 */
@Service
public class DefaultCriarResumoService implements CriarResumoUseCase {

    private final ResumoRepository resumoRepository;
    private  final DisciplinaRepository disciplinaRepository;

    /**
     * Construtor que injeta os repositórios necessários para operação.
     *
     * @param resumoRepository repositório para persistência de Resumos
     * @param disciplinaRepository repositório para busca de Disciplinas
     */
    public DefaultCriarResumoService(ResumoRepository resumoRepository, DisciplinaRepository disciplinaRepository){
        this.resumoRepository = resumoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }


    /**
     * {@inheritDoc}
     * <p>
     * Executa a criação de forma transacional. Primeiro, busca a disciplina
     * associada para garantir sua existência. Em seguida, cria o objeto de domínio
     * Resumo (o que dispara suas validações internas) e o persiste.
     * </p>
     * @param command O comando com os dados de criação. Não pode ser nulo.
     * @return O Resumo recém-criado.
     * @throws IllegalArgumentException se o comando for nulo.
     * @throws DisciplinaNaoEncontradaException se a disciplina informada no comando não existir.
     */
    @Override
    @Transactional // Garante a atomicidade da operação (ou tudo funciona, ou nada é salvo)
    public Resumo executar(CriarResumoCommand command) {
        // Validação da entrada do caso de uso
        Objects.requireNonNull(command, "O comando de criação não pode ser nulo.");

        // Busca a disciplina para garantir que ela existe
        Disciplina disciplina = disciplinaRepository.findById(command.disciplinaId())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(command.disciplinaId()));

        // Cria o objeto de domínio Resumo usando o metodo de fábrica
        // Isso dispara as validações internas do próprio Resumo (ex: título não pode ser vazio)
        Resumo novoResumo = Resumo.criar(
                UUID.randomUUID(), // Gera um novo ID para o resumo
                command.usuarioId(),
                command.titulo(),
                command.conteudo(),
                disciplina
        );

        // 3. Salva o novo resumo no repositório
        return resumoRepository.salvar(novoResumo);
    }



}