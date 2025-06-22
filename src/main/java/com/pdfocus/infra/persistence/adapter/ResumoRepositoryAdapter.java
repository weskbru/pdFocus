package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.infra.persistence.entity.ResumoEntity;
import com.pdfocus.infra.persistence.mapper.ResumoMapper;
import com.pdfocus.infra.persistence.repository.ResumoJpaRepository; // Sua interface Spring Data JPA

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa a porta de saída {@link ResumoRepository}
 * utilizando Spring Data JPA para persistência de dados de Resumo.
 */
@Component
public class ResumoRepositoryAdapter implements ResumoRepository {

    private final ResumoJpaRepository jpaRepository;

    public ResumoRepositoryAdapter(ResumoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Resumo salvar(Resumo resumo) {
        ResumoEntity resumoEntity = ResumoMapper.toEntity(resumo);
        ResumoEntity savedEntity = jpaRepository.save(resumoEntity);
        return ResumoMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resumo> buscarPorIdEUsuario(UUID id, UUID usuarioId) {
        Optional<ResumoEntity> optionalEntity = jpaRepository.findByIdAndUsuarioId(id, usuarioId);
        return optionalEntity.map(ResumoMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resumo> buscarTodosPorUsuario(UUID usuarioId) {
        List<ResumoEntity> entities = jpaRepository.findAllByUsuarioId(usuarioId);
        return ResumoMapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId) {
        List<ResumoEntity> entities = jpaRepository.findAllByDisciplina_IdAndUsuarioId(disciplinaId, usuarioId);
        return ResumoMapper.toDomainList(entities);
    }

    @Override
    @Transactional
    public boolean deletarPorIdEUsuario(UUID id, UUID usuarioId) {

        // O metodo no ResumoJpaRepository retorna o número de linhas deletadas (long).
        // Se o número for maior que 0, a deleção foi bem-sucedida.
        return jpaRepository.deleteByIdAndUsuarioId(id, usuarioId) > 0;
    }
}