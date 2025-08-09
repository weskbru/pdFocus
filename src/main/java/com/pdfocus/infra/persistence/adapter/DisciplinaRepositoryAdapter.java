package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.mapper.DisciplinaMapper;
import com.pdfocus.infra.persistence.repository.DisciplinaJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa a porta de saída {@link DisciplinaRepository}
 * utilizando Spring Data JPA para persistência de dados de Disciplina.
 */
@Component
public class DisciplinaRepositoryAdapter implements DisciplinaRepository {

    private final DisciplinaJpaRepository jpaRepository;

    public DisciplinaRepositoryAdapter(DisciplinaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Disciplina salvar(Disciplina disciplina) {
        DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(disciplina);
        DisciplinaEntity savedEntity = jpaRepository.save(disciplinaEntity);
        return DisciplinaMapper.toDomain(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> findById(UUID id) {
        Optional<DisciplinaEntity> optionalDisciplinaEntity = jpaRepository.findById(id);
        return optionalDisciplinaEntity.map(DisciplinaMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Disciplina> listaTodasPorUsuario(UUID usuarioId) {
        List<DisciplinaEntity> entities = jpaRepository.findAllByUsuarioId(usuarioId);
        return DisciplinaMapper.toDomainList(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deletarPorIdEUsuario(UUID id, UUID usuarioId) {
        DisciplinaEntity disciplinaParaDeletar = jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(id));

        jpaRepository.delete(disciplinaParaDeletar);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação segura busca a disciplina pelo seu ID e pelo ID do
     * usuário proprietário para garantir a autorização.
     * </p>
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> findByIdAndUsuarioId(UUID id, UUID usuarioId) {
        return jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .map(DisciplinaMapper::toDomain);
    }
}
