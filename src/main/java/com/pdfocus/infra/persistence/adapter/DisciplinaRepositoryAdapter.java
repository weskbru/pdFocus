package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.resumo.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.mapper.DisciplinaMapper;
import com.pdfocus.infra.persistence.repository.DisciplinaJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa a porta de saída {@link DisciplinaRepository}
 * utilizando Spring Data JPA para persistência de dados de Disciplina.
 * Esta classe faz a ponte entre a camada de aplicação e a infraestrutura de persistência.
 */
@Component
public class DisciplinaRepositoryAdapter implements DisciplinaRepository {

    private final DisciplinaJpaRepository jpaRepository;

    /**
     * Construtor para injeção de dependências.
     *
     * @param jpaRepository O repositório Spring Data JPA para DisciplinaEntity.
     */
    public DisciplinaRepositoryAdapter(DisciplinaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * {@inheritDoc}
     * Salva uma disciplina. Envolve converter o objeto de domínio para entidade JPA,
     * persistir usando o repositório JPA e converter a entidade salva de volta para o domínio.
     */
    @Override
    @Transactional
    public Disciplina salvar(Disciplina disciplina) {
        DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(disciplina);
        DisciplinaEntity savedEntety = jpaRepository.save(disciplinaEntity);
        return DisciplinaMapper.toDomain(savedEntety);
    }

    /**
     * {@inheritDoc}
     * Busca uma disciplina pelo seu ID. A entidade JPA é recuperada e então
     * mapeada para o objeto de domínio.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> findById(UUID id) {
        Optional<DisciplinaEntity> optionalDisciplinaEntity = jpaRepository.findById(id);
        return optionalDisciplinaEntity.map(DisciplinaMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     * Lista todas as disciplinas. As entidades JPA são recuperadas e então
     * mapeadas para uma lista de objetos de domínio.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Disciplina> listarTodas() {
        List<DisciplinaEntity> entities = jpaRepository.findAll();
        return DisciplinaMapper.toDomainList(entities);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação tenta deletar a {@link com.pdfocus.infra.persistence.entity.DisciplinaEntity}
     * correspondente ao ID fornecido. Antes de deletar, verifica se a entidade existe.
     * (ou similar) pode ser lançada, conforme o contrato da interface.
     *</p>
     */
    @Override
    @Transactional
    public void deletarPorId(UUID id) { // Supondo que sua porta tenha este método
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
        } else {
            // Lançar uma exceção como DisciplinaNaoEncontradaException seria uma boa prática aqui
            // para indicar que a disciplina que se tentou deletar não existe.
            // Ex: throw new DisciplinaNaoEncontradaException("Não foi possível deletar. Disciplina com ID " + id + " não encontrada.");
        }
    }


}
