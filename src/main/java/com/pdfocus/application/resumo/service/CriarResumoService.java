package com.pdfocus.application.resumo.service;


import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.port.entrada.CriarResumoUseCase;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import org.springframework.stereotype.Service;


import java.util.UUID;

/**
 * Serviço responsável por criar um novo resumo no sistema.
 *
 * Recebe dados através do comando {@link CriarResumoCommand}, valida a existência da disciplina,
 * cria a entidade Resumo e persiste usando o repositório.
 */
@Service
public class CriarResumoService implements CriarResumoUseCase {

    private final ResumoRepository resumoRepository;
    private  final DisciplinaRepository disciplinaRepository;

    /**
     * Construtor que injeta os repositórios necessários para operação.
     *
     * @param resumoRepository repositório para persistência de Resumos
     * @param disciplinaRepository repositório para busca de Disciplinas
     */
    public CriarResumoService(ResumoRepository resumoRepository, DisciplinaRepository disciplinaRepository){
        this.resumoRepository = resumoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }


    /**
     * Cria um novo resumo a partir dos dados do comando.
     *
     * @param command comando contendo dados para criação do resumo
     * @return o resumo criado e persistido
     * @throws DisciplinaNaoEncontradaException se a disciplina referenciada não for encontrada
     */
    @Override
    public Resumo execute(CriarResumoCommand command) {

        Disciplina disciplina = disciplinaRepository.findById(command.getIdDisciplina())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(command.getIdDisciplina()));

        return resumoRepository.salvar(
                Resumo.criar(
                        UUID.randomUUID(),
                        command.getIdUsuario(),
                        command.getTitulo(),
                        command.getConteudo(),
                        disciplina
                )
        );
    }



}