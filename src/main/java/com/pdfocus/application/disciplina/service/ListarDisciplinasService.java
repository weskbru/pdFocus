package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.ListarDisciplinasUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Serviço responsável por implementar o caso de uso de listar todas as disciplinas.
 * Esta classe obtém as disciplinas através de um {@link DisciplinaRepository}.
 *
 * <p>Nota sobre o pacote: Idealmente, para uma separação clara de contextos,
 * esta classe e sua interface {@link ListarDisciplinasUseCase} poderiam residir
 * em pacotes específicos do domínio 'disciplina', como
 * {@code com.pdfocus.application.disciplina.service} e
 * {@code com.pdfocus.application.disciplina.port.entrada} respectivamente.
 * </p>
 */
@Service
public class ListarDisciplinasService implements ListarDisciplinasUseCase {

    private final DisciplinaRepository disciplinaRepository;

    /**
     * Constrói uma nova instância de {@code ListarDisciplinasService}
     * com o repositório de disciplinas fornecido.
     *
     * @param disciplinaRepository O repositório a ser usado para buscar as disciplinas.
     * Não pode ser {@code null}.
     * @throws NullPointerException se {@code disciplinaRepository} for {@code null}.
     */
    public ListarDisciplinasService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository, "DisciplinaRepository não pode ser nulo.");
    }

    /**
     * Retorna uma lista contendo todas as disciplinas cadastradas.
     * A busca é delegada ao {@link DisciplinaRepository}.
     *
     * @return Uma {@link List} de {@link Disciplina} contendo todas as disciplinas.
     * Retorna uma lista vazia se nenhuma disciplina for encontrada.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Disciplina> listarTodas() {
        return disciplinaRepository.listarTodas();
    }
}