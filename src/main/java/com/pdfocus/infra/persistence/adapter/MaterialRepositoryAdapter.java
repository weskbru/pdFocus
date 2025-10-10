package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.MaterialEntity;
import com.pdfocus.infra.persistence.mapper.MaterialMapper;
import com.pdfocus.infra.persistence.repository.DisciplinaJpaRepository;
import com.pdfocus.infra.persistence.repository.MaterialJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Adaptador que implementa a porta de saída {@link MaterialRepository}
 * utilizando Spring Data JPA para a persistência de dados de Material.
 */
@Repository
public class MaterialRepositoryAdapter implements MaterialRepository {

    private final MaterialJpaRepository materialJpaRepository;
    private final DisciplinaJpaRepository disciplinaJpaRepository;

    public MaterialRepositoryAdapter(MaterialJpaRepository materialJpaRepository, DisciplinaJpaRepository disciplinaJpaRepository) {
        this.materialJpaRepository = materialJpaRepository;
        this.disciplinaJpaRepository = disciplinaJpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Material salvar(Material material) {
        // Busca a DisciplinaEntity para garantir a integridade da relação.
        DisciplinaEntity disciplinaEntity = disciplinaJpaRepository.findById(material.getDisciplinaId())
                .orElseThrow(() -> new IllegalStateException("A disciplina associada ao material não foi encontrada. ID:" + material.getDisciplinaId()));

        // Mapeia o modelo de domínio para a entidade JPA.
        MaterialEntity materialEntity = MaterialMapper.toEntity(material, disciplinaEntity);

        // Persiste a entidade no banco de dados.
        MaterialEntity savedEntity = materialJpaRepository.save(materialEntity);

        // Mapeia a entidade salva de volta para o modelo de domínio.
        return MaterialMapper.toDomain(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Material> listarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId) {
        List<MaterialEntity> entities = materialJpaRepository.findAllByDisciplina_IdAndUsuarioId(disciplinaId, usuarioId);
        return MaterialMapper.toDomainList(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Material> buscarPorIdEUsuario(UUID id, UUID usuarioId) {
        return materialJpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .map(MaterialMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deletarPorIdEUsuario(UUID id, UUID usuarioId) {
        if (materialJpaRepository.findByIdAndUsuarioId(id, usuarioId).isEmpty()) {
            throw new MaterialNaoEncontradoException(id);
        }
        materialJpaRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long countByUsuario(Usuario usuario) {
        return materialJpaRepository.countByUsuarioId(usuario.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Material> buscar5MaisRecentesPorUsuario(Usuario usuario) {
        List<MaterialEntity> entities = materialJpaRepository.findFirst5ByUsuarioIdOrderByDataUploadDesc(usuario.getId());
        return MaterialMapper.toDomainList(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Material> buscarPorId(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return materialJpaRepository.findById(id)
                .map(MaterialMapper::toDomain);
    }

    @Override
    public Page<Material> buscarPorDisciplinaDeFormaPaginada(UUID disciplinaId, Pageable pageable) {
        // 1. Chama o método "mágico" do Spring Data JPA
        Page<MaterialEntity> paginaDeEntidades = materialJpaRepository.findByDisciplinaId(disciplinaId, pageable);

        // 2. Usa o seu Mapper para converter a página de Entidades (JPA) para uma página de Modelos de Domínio (Core)
        return paginaDeEntidades.map(MaterialMapper::toDomain);
    }

    @Override
    @Transactional
    public void deletarTodosPorDisciplinaId(UUID disciplinaId) {
        // Busca todos os materiais da disciplina
        List<MaterialEntity> materiais = materialJpaRepository.findByDisciplinaId(disciplinaId);

        // Deleta em lote (mais eficiente)
        materialJpaRepository.deleteAllInBatch(materiais);

        // Log para debug (opcional)
        System.out.println("✅ Deletados " + materiais.size() + " materiais da disciplina: " + disciplinaId);
    }

}

