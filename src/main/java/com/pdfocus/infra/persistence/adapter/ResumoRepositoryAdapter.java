package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.resumo.ResumoNaoEncontradoException;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.ResumoEntity;
import com.pdfocus.infra.persistence.mapper.ResumoMapper;
import com.pdfocus.infra.persistence.repository.ResumoJpaRepository;

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
    public void deletarPorIdEUsuario(UUID id, UUID usuarioId) {

        // Usamos o metodo de busca que já valida a propriedade do resumo
        ResumoEntity resumoParaDeletar = jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResumoNaoEncontradoException(id));

        // Se encontrou, o jpaRepository o deleta.
        jpaRepository.delete(resumoParaDeletar);
    }

    @Override
    public long countByUsuario(Usuario usuario) {
        return jpaRepository.countByUsuarioId(usuario.getId());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementa a lógica de "cascade delete" da aplicação.
     * 1. Encontra todas as entidades filhas (Resumos) usando o JPA.
     * 2. Deleta todas elas em um lote (batch), que é mais performático.
     * </p>
     */
    @Override
    @Transactional
    public void deletarTodosPorDisciplinaId(UUID disciplinaId) {
        // 1. Busca todos os resumos da disciplina (usando o Passo 2)
        List<ResumoEntity> resumos = jpaRepository.findByDisciplinaId(disciplinaId);

        // 2. Se houver resumos, deleta em lote
        if (resumos != null && !resumos.isEmpty()) {
            jpaRepository.deleteAllInBatch(resumos);
            // System.out.println("✅ Deletados " + resumos.size() + " resumos da disciplina: " + disciplinaId);
        }
    }
}